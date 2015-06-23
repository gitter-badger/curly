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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import curly.tagger.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author João Evangelista
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagMessage {

	private String id;
	private String name;

	public TagMessage(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@NotNull
	public static TagMessage from(@NotNull Tag tag) {
		return new TagMessage(tag.getId(), tag.getName());
	}

	@NotNull
	public Tag toTag() {
		return new Tag(this.id, this.name);
	}
}
