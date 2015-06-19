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
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import curly.artifact.integration.event.CreatedArtifactEvent;
import curly.artifact.model.Artifact;
import curly.commons.github.OctoUser;
import curly.commons.logging.annotation.Loggable;
import curly.commons.security.negotiation.ResourceOperationsResolverAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
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

	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public DefaultArtifactService(ArtifactRepository artifactRepository) {
		this.repository = artifactRepository;
	}

	@Loggable
	@Override
	@HystrixCommand(fallbackMethod = "defaultFindAll")
	public Observable<Optional<Page<Artifact>>> findAll(Pageable pageable) {
		log.debug("Finding for page {}", pageable.getPageNumber());
		return new ObservableResult<Optional<Page<Artifact>>>() {
			@Override
			public Optional<Page<Artifact>> invoke() {
				return Optional.ofNullable(repository.findAll(pageable));
			}
		};
	}

	@Loggable
	@Override
	@Retryable
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

	private Optional<Artifact> handle(Artifact save) {
		if (save != null) {
			publishEvents(save);
			return Optional.of(save);
		} else {
			return Optional.empty();
		}
	}

	private void publishEvents(Artifact entity) {
		applicationEventPublisher.publishEvent(new CreatedArtifactEvent(entity));
	}

	@Loggable
	@Override
	@HystrixCommand(fallbackMethod = "defaultFindOne")
	public Observable<Optional<Artifact>> findOne(String id) {
		log.debug("Finding for {}", id);
		return new ObservableResult<Optional<Artifact>>() {
			@Override
			public Optional<Artifact> invoke() {
				return Optional.ofNullable(repository.findOne(id));
			}
		};
	}


	@Async
	@Override
	@Loggable
	@Retryable
	@HystrixCommand
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