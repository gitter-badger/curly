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
package curly.tagger.command;

import com.google.common.base.Optional;
import curly.tagger.model.Tag;
import rx.Observable;

import java.util.List;
import java.util.Set;

/**
 * @author João Evangelista
 */
public interface TagCommand {
	Observable<Optional<Tag>> get(String tag);

	Observable<Optional<List<Tag>>> like(String tag);

	void save(Set<Tag> tag);
}