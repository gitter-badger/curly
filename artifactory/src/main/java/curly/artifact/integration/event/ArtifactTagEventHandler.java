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
package curly.artifact.integration.event;

import curly.artifact.integration.service.EventEmitter;
import curly.artifact.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author João Evangelista
 */
@Slf4j
@Component
public class ArtifactTagEventHandler implements ApplicationListener<ArtifactTagEvent> {

	private final EventEmitter<Set<Tag>> eventEmitter;

	@Autowired
	public ArtifactTagEventHandler(EventEmitter<Set<Tag>> eventEmitter) {
		this.eventEmitter = eventEmitter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onApplicationEvent(ArtifactTagEvent event) {
		log.info("Received ArtifactTagEvent event, stating processing...");
		Object source = event.getSource();
		if (source instanceof Set) {
			this.eventEmitter.emit(((Set<Tag>) source));
			log.info("Event sent to processor...");
		} else {
			log.warn("Cannot process event {}", event.toString(), "maybe not a catchable instance ?");
		}
	}


}
