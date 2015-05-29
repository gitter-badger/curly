package curly.commons.config.cloud.actuator.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.actuator.properties.AmqpActuatorProperties;
import org.springframework.stereotype.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Component
@ConfigurationProperties(prefix = "spring.cloud.actuator.client", ignoreInvalidFields = true)
public class AmqpClientActuatorProperties {

	private String queue;

	private String exchange;

	public String getQueue() {
		return (queue == null ? AmqpActuatorProperties.DEFAULT_QUEUE : queue);
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getExchange() {
		return (exchange == null ? AmqpActuatorProperties.DEFAULT_EXCHANGE : exchange);
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
}
