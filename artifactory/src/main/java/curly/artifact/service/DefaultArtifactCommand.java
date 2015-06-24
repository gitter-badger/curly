package curly.artifact.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import curly.artifact.model.Artifact;
import curly.commons.github.OctoUser;
import curly.commons.logging.annotation.Loggable;
import curly.commons.stereotype.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import rx.Observable;

import java.util.Collections;
import java.util.Optional;

/**
 * @author João Evangelista
 */
@Command
@Retryable
public class DefaultArtifactCommand implements ArtifactCommand {

	private static final String EXECUTION_ISOLATION = "execution.isolation.strategy";

	private static final String SEMAPHORE = "SEMAPHORE";

	private final ApplicationEventPublisher applicationEventPublisher;

	private final ArtifactService artifactService;

	@Autowired
	public DefaultArtifactCommand(ApplicationEventPublisher applicationEventPublisher, ArtifactService artifactService) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.artifactService = artifactService;
	}

	@Override
	@Loggable
	@HystrixCommand(fallbackMethod = "defaultFindAll", ignoreExceptions = IllegalStateException.class,
			commandProperties = @HystrixProperty(name = EXECUTION_ISOLATION, value = SEMAPHORE))
	public Observable<Optional<Page<Artifact>>> findAllByPage(Pageable pageable) {
		return new ObservableResult<Optional<Page<Artifact>>>() {
			@Override public Optional<Page<Artifact>> invoke() {
				return Optional.ofNullable(artifactService.findAll(pageable));
			}
		};
	}

	@Override
	@Loggable
	@HystrixCommand(ignoreExceptions = IllegalArgumentException.class)
	public Observable<Optional<Artifact>> save(Artifact artifact, OctoUser octoUser) {
		return new ObservableResult<Optional<Artifact>>() {
			@Override public Optional<Artifact> invoke() {
				return Optional.ofNullable(artifactService.save(artifact, octoUser));
			}
		};
	}

	@Override
	@Loggable
	@HystrixCommand(fallbackMethod = "defaultFindOne", ignoreExceptions = IllegalArgumentException.class,
			commandProperties = @HystrixProperty(name = EXECUTION_ISOLATION, value = SEMAPHORE))
	public Observable<Optional<Artifact>> findOne(String id) {
		return new ObservableResult<Optional<Artifact>>() {
			@Override public Optional<Artifact> invoke() {
				return Optional.ofNullable(artifactService.findOne(id));
			}
		};
	}


	@Override
	@Loggable
	@HystrixCommand(ignoreExceptions = IllegalArgumentException.class)
	public void delete(String id, OctoUser octoUser) {
		artifactService.delete(id, octoUser);
	}


	@Loggable
	@SuppressWarnings("unused")
	@HystrixCommand
	private Optional<Page<Artifact>> defaultFindAll(Pageable pageable) {
		return Optional.of(new PageImpl<>(Collections.emptyList()));
	}

	@Loggable
	@SuppressWarnings("unused")
	@HystrixCommand
	private Optional<Artifact> defaultFindOne(String id) {
		return Optional.of(new Artifact(id));
	}


}
