package curly.tagger.listener;

import curly.commons.config.reactor.Reactor;
import curly.tagger.command.InsertCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.bus.EventBus;
import reactor.spring.context.annotation.Consumer;
import reactor.spring.context.annotation.Selector;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author João Evangelista
 */
@Slf4j
@Consumer
class EventHandler {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private final EventBus eventBus;
	private final InsertCommand insertCommand;

	@Inject
	public EventHandler(@NotNull @Reactor EventBus eventBus, @NotNull @Qualifier("insertCommand") InsertCommand insertCommand) {
		this.eventBus = eventBus;
		this.insertCommand = insertCommand;
	}

	@Selector("tag.bus")
	public void handle(TagMessage message) {
		log.info("Initiating processor for tag message received on Event Bus");
		if (message != null) {
			insertCommand.save(message.toTag());
			if (log.isTraceEnabled()) log.trace("Tag service operation emitted");
		} else {
			log.error("Message received on Event Bus was NULL!!");
		}
	}
}
