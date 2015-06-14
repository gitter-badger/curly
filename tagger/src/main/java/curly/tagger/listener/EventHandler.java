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
package curly.tagger.listener;

import curly.commons.config.reactor.Reactor;
import curly.tagger.command.InsertCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import reactor.bus.EventBus;
import reactor.spring.context.annotation.Consumer;
import reactor.spring.context.annotation.Selector;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handle event dispatched by the event bus on {@link DefaultTagEventListener#onReceive(Message)} )}
 *
 * @author João Evangelista
 */
@Slf4j
@Consumer
public class EventHandler {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private final EventBus eventBus;
	private final InsertCommand insertCommand;

	@Inject
	public EventHandler(@NotNull @Reactor EventBus eventBus,
						@NotNull @Qualifier("insertCommand") InsertCommand insertCommand) {
		this.eventBus = eventBus;
		this.insertCommand = insertCommand;
	}


	@Selector("tag.bus")
	public void handle(Set<TagMessage> message) {
		log.info("Initiating processor for tag message received on Event Bus");
			insertCommand.save(message
					.stream()
					.map(TagMessage::toTag)
					.collect(Collectors.toSet()));
	}
}
