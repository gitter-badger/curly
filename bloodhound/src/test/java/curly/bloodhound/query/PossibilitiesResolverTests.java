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

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author João Evangelista
 */
public class PossibilitiesResolverTests {

	private static final String DEFAULT_QUERY = "cat:bar t:{\"too foo\", yoo,loo} language:\"java\"";

	private QueryResolver resolver;

	private MultiValueMap<String, String> map;

	@Before
	public void setUp() throws Exception {
		this.resolver = new PossibilitiesResolver();
		this.map = QueryParser.resolveMultiParameter(DEFAULT_QUERY);

	}

	@Test
	public void testResolve() throws Exception {
		MultiValueMap<String, String> resolvedMap = resolver.resolve(map);
		assertThat("Must contain category key", resolvedMap, hasKey("category"));
		assertThat("Must contain tags key", resolvedMap, hasKey("tags"));
		assertThat("Must contain language key", resolvedMap, hasKey("language"));
		assertThat("Must be an array of 3", resolvedMap.get("tags"), hasSize(3));
		assertThat("Must contain java string", resolvedMap.get("language").get(0), equalToIgnoringCase("java"));
	}
}
