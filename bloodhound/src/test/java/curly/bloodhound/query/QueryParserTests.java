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
import org.junit.Test;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static curly.bloodhound.query.QueryUtils.fix;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author João Evangelista
 */
public class QueryParserTests {


	public static final String REGEX_INNER_CURLY = "\\{(.*?)\\}";

	public static final String SPLIT_SPACES = "\\s+(?![^\\{]*\\})";

	public static final String DEFAULT_QUERY = "foo:bar goo:{too, yoo,loo} roo:{ree rii}";

	@Test
	public void testSplitSpace() throws Exception {
		List<String> list = getSplits(DEFAULT_QUERY);
		System.out.println(list);
		assertThat(list.size(), is(3));
		assertThat(list, contains("foo:bar", "goo:{too, yoo,loo}", "roo:{ree rii}"));
	}

	@NotNull private static List<String> getSplits(String s) {
		return Arrays.asList(s.split(SPLIT_SPACES));
	}

	@Test
	public void testKeyValue() throws Exception {
		Map<String, String> kvMap = getMappedKeyValue(getSplits(DEFAULT_QUERY));
		assertTrue(kvMap.get("foo").equals("bar"));
	}

	@NotNull private static Map<String, String> getMappedKeyValue(List<String> list) {
		Map<String, String> kvMap = new HashMap<>(0);
		list.forEach(item -> {
			String[] kv = item.split(":");
			if (kv.length == 2) {
				kvMap.put(kv[0], kv[1]);
			}
		});
		System.out.println("Mapped Key Value " + kvMap);
		return kvMap;
	}

	@Test
	public void testIfContainsCurlyCleanAndSplitIntoMap() throws Exception {
		final MultiValueMap<String, String> multimap = QueryParser.resolveMultiParameter(DEFAULT_QUERY);
		assertThat(multimap.get("foo"), contains("bar"));
	}

	@Test
	public void testMalformedQuery() throws Exception {
		String mq = "foo:bar goo:{too, yoo,loo roo:{ree rii}";
		Map<String, String> map = getMappedKeyValue(getSplits(mq));
		fix(map);
		map.forEach((k, v) -> System.out.println(k + " " + v));
		assertThat(map.get("goo"), equalTo("{too}"));
	}
}
