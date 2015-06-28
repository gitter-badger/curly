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

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author João Evangelista
 */
final class QueryUtils {

	protected static final String L_CURLY = "{";

	protected static final String R_CURLY = "}";

	/**
	 * Fix any curly mismatched on the query Map value, replacing it with the appropriate value
	 *
	 * @param map current info Map
	 */
	static void fix(Map<String, String> map) {
		map.forEach((key, value) -> {
			boolean containsOnlyRightCurly = (value.contains(R_CURLY) && !value.contains(L_CURLY));
			boolean containsOnlyLeftCurly = (value.contains(L_CURLY) && !value.contains(R_CURLY));
			if (containsOnlyRightCurly) {
				String committedValue = value.trim().replace(",", "");
				String newValue = L_CURLY + committedValue;
				map.replace(key, value, newValue);
			} else if (containsOnlyLeftCurly) {
				String committedValue = value.trim().replace(",", "");
				String newValue = committedValue + R_CURLY;
				map.replace(key, value, newValue);
			}
		});
	}

	@Nullable static List<String> trimElements(Collection<String> strings) {
		if (strings == null) return null;
		List<String> output = new ArrayList<>(strings.size());
		strings.stream().forEach(e -> output.add(e.trim()));
		return output;
	}

	static String cleanDelimiters(String inner) {
		return inner.replace(L_CURLY, "").replace(R_CURLY, "");
	}

	static List<String> toUnquotedList(String[] quoted) {
		List<String> unquotedArray = new ArrayList<>(quoted.length);
		for (String s : quoted) {
			if (s.contains("\"")) {
				unquotedArray.add(unquote(s));
			} else {
				unquotedArray.add(s);
			}
		}
		return unquotedArray;
	}

	static String unquote(String quoted) {
		return quoted.replace("\"", "");
	}
}
