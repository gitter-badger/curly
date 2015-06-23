/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package curly.artifact.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import curly.artifact.integration.event.CreatedArtifactEvent;
import curly.artifact.model.Artifact;
import curly.commons.github.OctoUser;
import curly.commons.logging.annotation.Loggable;
import curly.commons.security.negotiation.ResourceOperationsResolverAdapter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rx.Observable;

import java.util.Collections;
import java.util.Optional;

/**
 * @author João Pedro Evangelista
 */
@Slf4j
@Service
public class DefaultArtifactService extends ResourceOperationsResolverAdapter<Artifact, OctoUser>
		implements ArtifactService, ApplicationEventPublisherAware {

	private final ArtifactRepository repository;

	private ApplicationEventPublisher applicationEventPublisher = null;

	@Autowired
	public DefaultArtifactService(ArtifactRepository artifactRepository) {
		this.repository = artifactRepository;
	}

	@NotNull
	@Loggable
	@Override
	@HystrixCommand(fallbackMethod = "defaultFindAll", ignoreExceptions = IllegalStateException.class)
	public Observable<Optional<Page<Artifact>>> findAll(@NotNull Pageable pageable) {
		Assert.notNull(pageable, "Page information must be not null!");
		log.debug("Finding for page {}", pageable.getPageNumber());
		return new ObservableResult<Optional<Page<Artifact>>>() {
			@Override
			public Optional<Page<Artifact>> invoke() {
				Page<Artifact> all = repository.findAll(pageable);
				System.out.println("------------>" + all);
				return Optional.ofNullable(all);
			}
		};
	}

	@NotNull
	@Loggable
	@Override
	@Retryable
	@HystrixCommand(ignoreExceptions = IllegalStateException.class)
	public Observable<Optional<Artifact>> save(Artifact artifact, OctoUser octoUser) {
		log.debug("Saving entity {} ...", artifact);
		return new ObservableResult<Optional<Artifact>>() {
			@Override
			public Optional<Artifact> invoke() {
				checkMatch(artifact, octoUser);
				return handle(repository.save(artifact));
			}
		};
	}

	@NotNull
	@Loggable
	@Override
	@HystrixCommand(fallbackMethod = "defaultFindOne", ignoreExceptions = IllegalStateException.class, commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
	public Observable<Optional<Artifact>> findOne(String id) {
		Assert.hasText(id, "Id must be not null or empty!");
		log.debug("Finding for {}", id);
		return new ObservableResult<Optional<Artifact>>() {
			@Override
			public Optional<Artifact> invoke() {
				return Optional.ofNullable(repository.findOne(id));
			}
		};
	}

	@Override
	@Loggable
	@Retryable
	@HystrixCommand(ignoreExceptions = IllegalStateException.class)
	public void delete(String id, OctoUser user) {
		Assert.notNull(user, "OctoUser must be not null");
		Assert.hasText(id, "Id must be not empty");
		log.debug("Looking for entity with id {}", id);
		findOne(id)
				.map(opt -> opt.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
				.filter(artifact -> isOwnedBy(artifact, user))
				.doOnError(throwable ->
						log.error("Cannot process #delete({},{}) nested exception is, {}", id, user.getId(), throwable))
				.subscribe(repository::delete);
	}

	private Optional<Artifact> handle(@Nullable Artifact save) {
		if (save != null) {
			applicationEventPublisher.publishEvent(new CreatedArtifactEvent(save));
			return Optional.of(save);
		} else {
			return Optional.empty();
		}
	}

	@Loggable
	@SuppressWarnings("unused")
	@HystrixCommand
	private Optional<Page<Artifact>> defaultFindAll(Pageable pageable) {
		log.debug("Falling back with empty collection");
		return Optional.of(new PageImpl<>(Collections.emptyList()));
	}

	@Loggable
	@SuppressWarnings("unused")
	@HystrixCommand
	private Optional<Artifact> defaultFindOne(String id) {
		log.debug("Falling back with {}", id);
		return Optional.of(new Artifact(id));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
