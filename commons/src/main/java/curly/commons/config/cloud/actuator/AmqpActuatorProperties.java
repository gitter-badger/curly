package curly.commons.config.cloud.actuator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author João Pedro Evangelista
 */
@Component
@ConfigurationProperties(prefix = "spring.cloud.actuator")
public class AmqpActuatorProperties {

	public static final String HEADER_MACHINE_NAME = "AppMachineName";

	public static final long FIXED_RATE = 1000;

	public static final String DEFAULT_EXCHANGE = "actuator.exchange";

	public static final String DEFAULT_QUEUE = "actuator.queue";

	private String exchange;

	private String queue;

	private String name;

	public String getExchange() {
		return (exchange == null ? DEFAULT_EXCHANGE : exchange);
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getQueue() {
		return (queue == null ? DEFAULT_QUEUE : queue);
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getName() {
		return name;
	}

}
