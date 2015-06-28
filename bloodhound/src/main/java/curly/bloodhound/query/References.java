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

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @author João Evangelista
 */
final class References {

	private static final String CAT = "cat";

	private static final String CATEGORY = "category";

	private static final String C = "c";

	private static final String LANG = "lang";

	private static final String LANGUAGE = "language";

	private static final String L = "l";

	private static final String TAGS = "tags";

	private static final String T = "t";

	private static final Set<String> category = new ImmutableSet.Builder<String>().add(C, CAT, CATEGORY).build();

	private static final Set<String> language = new ImmutableSet.Builder<String>().add(L, LANG, LANGUAGE).build();

	private static final Set<String> tags = new ImmutableSet.Builder<String>().add(T, TAGS).build();

	private References() {
		//no-impl
	}

	public static Set<String> category() {
		return category;
	}

	public static Set<String> language() {
		return language;
	}

	public static Set<String> tags() {
		return tags;
	}

	public static boolean matches(String key, Set<String> refs) {
		for (String s : refs) {
			if (s.equals(key)) {
				return true;
			}
		}
		return false;
	}
}
