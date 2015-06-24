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
package curly.tagger.model;

import lombok.Data;

import java.util.List;

/**
 * @author João Evangelista
 */
@Data
public final class SearchResult {


	private final List<Tag> result;

	private final int size;

	public SearchResult(List<Tag> result) {
		this.result = result;
		size = result.size();
	}
}
