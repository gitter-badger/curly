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
import org.springframework.util.MultiValueMap;

import javax.inject.Inject;

/**
 * @author João Evangelista
 */
@Component
public class SearchTranspiler implements Transpiler {

	private final QueryResolver resolver;

	@Inject
	public SearchTranspiler(QueryResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public MultiValueMap<String, String> execute(String query) {
		Assert.hasText(query, "A query must have text!");
		MultiValueMap<String, String> parameter = QueryParser.resolveMultiParameter(query);
		return resolver.resolve(parameter);
	}
}
