package curly.commons.cloud.actuator.emitter;

import curly.commons.config.cloud.actuator.AmqpActuatorProperties;
import curly.commons.support.MessageHelper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.BeansEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Joao Pedro Evangelista
 */
public class Emitter implements MetricsEmitter, BeansEmitter {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private MetricsEndpoint metricsEndpoint;

	@Autowired
	private BeansEndpoint beansEndpoint;

	@Autowired
	private MessageConverter messageConverter;

	@Autowired
	private AmqpActuatorProperties amqpActuatorProperties;

	@Override
	@Scheduled(fixedRate = AmqpActuatorProperties.FIXED_RATE)
	public void emitBeans() {
		System.out.println("Sending message...");
		amqpTemplate.send(amqpActuatorProperties.getQueue(), MessageHelper.convert(beansEndpoint.invoke(), messageConverter, "foo"));
	}

	@Override
	@Scheduled(fixedRate = AmqpActuatorProperties.FIXED_RATE)
	public void emitMetrics() {
		System.out.println("Sending message...");
		amqpTemplate.send(amqpActuatorProperties.getQueue(), MessageHelper.convert(metricsEndpoint.invoke(), messageConverter, "foo"));

	}


}
