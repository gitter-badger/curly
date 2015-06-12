package curly.tagger.listener;

import curly.commons.config.reactor.Reactor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;

/**
 * @author João Evangelista
 */
@Slf4j
@Component
class DefaultTagEventListener {

	private EventBus eventBus;

	@Inject
	DefaultTagEventListener(@Reactor EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@RabbitListener(queues = "tag.queue")
	public void onReceive(@Payload Object o) {
		if (o instanceof TagMessage) {
			log.info("Received message on tag.queue, notifying Bus...");
			eventBus.notify("tag.bus", Event.wrap((TagMessage) o));
		}
	}


}
