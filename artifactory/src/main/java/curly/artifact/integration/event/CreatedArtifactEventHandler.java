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
import curly.artifact.model.Artifact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author João Evangelista
 */
@Slf4j
@Component
class CreatedArtifactEventHandler implements ApplicationListener<CreatedArtifactEvent> {

	private final List<EventEmitter<Artifact>> eventEmitters;

	@Autowired
	public CreatedArtifactEventHandler(List<EventEmitter<Artifact>> eventEmitters) {
		Assert.notNull(eventEmitters, "EventEmitters, must be not null!");
		this.eventEmitters = eventEmitters;
	}

	@Override
	public void onApplicationEvent(CreatedArtifactEvent event) {
		Object source = event.getSource();
		log.info("Got event source {} on handler", source);
		if (source instanceof Artifact) {
			log.info("Emitting source of event of type Artifact...");
			this.eventEmitters.stream().forEach(eventEmitter -> eventEmitter.emit((Artifact) source));
		} else {
			log.warn("Not a catchable instance type from event source, source was" + source + "!!");
		}
	}
}
