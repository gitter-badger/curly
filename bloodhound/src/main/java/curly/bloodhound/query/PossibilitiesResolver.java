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
package curly.bloodhound.query;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static curly.bloodhound.query.References.*;

/**
 * Manipulates the key of each property in the query, resulting in a collection of possibilities to let user choose them
 * then we can match each of these keys to a fixed set of predefined ones and "fix" the key to our pattern
 *
 * @author João Evangelista
 */
@Component
class PossibilitiesResolver implements QueryResolver {

	private static final String ES_CATEGORY = "category";

	private static final String ES_TAGS = "tags";

	private static final String ES_LANG = "language";

	/**
	 * Main method to convert the user inputted keys into our pattern
	 *
	 * @param raw the map containing not standardized keys
	 * @return transformed new map with correct keys
	 */
	@Override
	public MultiValueMap<String, String> resolve(MultiValueMap<String, String> raw) {
		Assert.notNull(raw, "Raw MultiValueMap must be not null!");
		MultiValueMap<String, String> output = new LinkedMultiValueMap<>(raw.size());
		raw.forEach((key, value) -> transform(output, key, value));
		return output;
	}

	/**
	 * Check for matches on our acceptable set of properties and put the current value with normalized key
	 *
	 * @param output the current output map
	 * @param key    map key
	 * @param value  map value
	 */
	private void transform(MultiValueMap<String, String> output, String key, List<String> value) {
		if (matches(key, category())) {
			output.put(ES_CATEGORY, value);
		} else if (matches(key, language())) {
			output.put(ES_LANG, value);
		} else if (matches(key, tags())) {
			output.put(ES_TAGS, value);
		}
	}

}
