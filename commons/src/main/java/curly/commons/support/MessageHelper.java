package curly.commons.support;

import curly.commons.config.cloud.actuator.AmqpActuatorProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Joao Pedro Evangelista
 */
public abstract class MessageHelper {

	public static Message convert(Object payload, MessageConverter messageConverter, String name) {
		return messageConverter.toMessage(payload, messageProperties(name));
	}

	private static MessageProperties messageProperties(String name) {
		MessageProperties properties = new MessageProperties();
		if (name != null) {
			properties.setHeader(AmqpActuatorProperties.HEADER_MACHINE_NAME, getMachineName() + name);
		} else {
			properties.setHeader(AmqpActuatorProperties.HEADER_MACHINE_NAME, getMachineName());
		}
		properties.setContentType("application/json");
		return properties;
	}

	private static String getMachineName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "Unknown";
		}
	}
}
