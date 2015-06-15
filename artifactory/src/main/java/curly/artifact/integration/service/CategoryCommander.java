package curly.artifact.integration.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import curly.artifact.model.Artifact;
import curly.commons.logging.annotation.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author João Evangelista
 */
@Slf4j
@Service
public class CategoryCommander implements EventEmitter<Artifact> {

	private final AmqpTemplate amqpTemplate;

	private final CategoryClient categoryClient;

	@Autowired
	public CategoryCommander(AmqpTemplate amqpTemplate, CategoryClient categoryClient) {
		this.amqpTemplate = amqpTemplate;
		this.categoryClient = categoryClient;
	}


	@Override
	@Loggable
	@Retryable
	@HystrixCommand(fallbackMethod = "emitFallback")
	public void emit(Artifact artifact) {
		log.info("Emitting event of source {}", artifact);
		amqpTemplate.convertAndSend("category.queue", artifact.getCategory());
	}


	@Loggable
	@Retryable
	@HystrixCommand
	public void emitFallBack(Object source) {
		Artifact artifact = (Artifact) source;
		log.info("Fallback starting, sending via HTTP, source {}", artifact);
		categoryClient.postEvent(artifact.getCategory());
	}
}
