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
package curly.tagger.service;

import curly.tagger.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Simple service layer to encapsulate repository access
 *
 * @author João Evangelista
 */
@Slf4j
@Service
class DefaultTagService implements TagService {

	private final TagRepository repository;

	@Inject
	DefaultTagService(TagRepository repository) {
		Assert.notNull(repository, "TagRepository must be not null!");
		this.repository = repository;
	}

	/**
	 * Save and if there is already a tag with the name, suppress it!
	 *
	 * @param tags to be saved
	 */
	@Override
	public void save(Set<Tag> tags) {
		tags.stream().forEach(tag -> {
			try {
				this.repository.save(tag);
			} catch (DuplicateKeyException e) {
				//just log and ignore
				log.debug("Received duplicated key", e);
			}
		});
	}

	/**
	 * Find the tag of param
	 * @param tag to look up for
	 * @return a tag if found or null otherwise
	 */

	@Override
	public Tag find(String tag) {
		return this.repository.findByName(tag);
	}

	/**
	 * Query the database for the first 10 result matching pattern of param
	 * @param tag the tag, or part of it to search for
	 * @return 10 top results
	 */

	@Override
	public List<Tag> query(String tag) {
		return this.repository.findTop10ByNameLike(tag);
	}
}
