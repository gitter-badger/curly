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

import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static curly.bloodhound.query.QueryUtils.*;
import static org.springframework.util.StringUtils.delimitedListToStringArray;

/**
 * @author João Evangelista
 */
final class QueryParser {

	private static final String REGEX_INNER_CURLY = "\\{(.*?)\\}";

	private static final String SPLIT_SPACES = "\\s+(?![^\\{]*\\})";

	private static final String SPLIT_SPACE_QUOTED = "([^\"]\\S*|\".+?\")\\s*";

	private static final String COMMA = ",";

	@NotNull static MultiValueMap<String, String> resolveMultiParameter(String query) {
		final MultiValueMap<String, String> multimap = new LinkedMultiValueMap<>();

		convertToMap(query).forEach((key, value) -> {
			if (value.contains(L_CURLY) && value.contains(R_CURLY)) {
				Matcher matcher = Pattern.compile(REGEX_INNER_CURLY).matcher(value);
				if (matcher.find()) {
					String cleaned = cleanDelimiters(matcher.group());
					if (cleaned.contains(COMMA)) {
						multimap.put(key, getCommaDelimitedValue(cleaned));
					} else {
						multimap.put(key, getSpaceSplicedValue(cleaned));
					}
				}
			} else {
				multimap.add(key, unquote(value));
			}
		});

		return multimap;
	}

	@NotNull private static Map<String, String> convertToMap(String query) {
		Map<String, String> kvMap = new HashMap<>(0);
		Arrays.asList(query.split(SPLIT_SPACES)).stream().forEach(item -> {
			String[] kv = item.split(":");
			if (kv.length == 2) {
				kvMap.put(kv[0].toLowerCase(), kv[1]);
			}
		});
		return kvMap;
	}

	private static List<String> getCommaDelimitedValue(String cleaned) {
		return trimElements(Arrays.asList(delimitedListToStringArray(unquote(cleaned), COMMA)));
	}

	private static List<String> getSpaceSplicedValue(String cleaned) {
		return trimElements(toUnquotedList(cleaned.split(SPLIT_SPACE_QUOTED)));
	}

	private static void transform(MultiValueMap<String, String> multimap, String key, String inner) {

	}


}
