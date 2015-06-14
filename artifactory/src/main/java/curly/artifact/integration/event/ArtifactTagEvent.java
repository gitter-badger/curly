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

import curly.artifact.model.Artifact;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * Event to handle tags when a new artifact is created, it is all based on the {@link Artifact#getTags()}
 * do not using this it will cause ClassCastExceptions
 *
 * @author João Evangelista
 */
public class ArtifactTagEvent extends ApplicationEvent {

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the component that published the event (never {@code null})
	 */
	public ArtifactTagEvent(Object source) {
		super(source);
		Assert.isInstanceOf(Set.class, source, "The source must be a Set<Tag> !! This will propagate errors through the chain!!");
	}

}
