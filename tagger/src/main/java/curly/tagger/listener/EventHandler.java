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
import curly.tagger.command.WriterCommand;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import reactor.bus.EventBus;
import reactor.spring.context.annotation.Consumer;
import reactor.spring.context.annotation.Selector;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handle event dispatched by the event bus on {@link DefaultTagEventListener#onReceive(org.springframework.amqp.core.Message)} )}
 *
 * @author João Evangelista
 */
@Slf4j
@Consumer
public class EventHandler {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private final EventBus eventBus;

	private final WriterCommand writerCommand;

	@Inject
	public EventHandler(@NotNull @Reactor EventBus eventBus,
						@NotNull @Qualifier("writerCommand") WriterCommand writerCommand) {
		Assert.notNull(eventBus, "EventBus must be not null!");
		Assert.notNull(writerCommand, "WriterCommand must be not null!");
		this.eventBus = eventBus;
		this.writerCommand = writerCommand;
	}


	@Selector("tag.bus")
	public void handle(@NotNull Set<TagMessage> message) {
		log.info("Initiating processor for tag message" + message + " received on Event Bus");
		writerCommand.save(message
				.stream()
				.map(TagMessage::toTag)
				.collect(Collectors.toSet()));
	}
}
