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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * @author João Evangelista
 */
@Service
class TagServiceImpl implements TagService {

	private final TagRepository repository;
	private final MongoTemplate mongoTemplate;

	@Inject
	TagServiceImpl(TagRepository repository, MongoTemplate mongoTemplate) {
		this.repository = repository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void save(Set<Tag> tag) {
		this.repository.save(tag);
	}

	@Override
	public Tag find(String tag) {
		return this.repository.findByName(tag);
	}

	@Override
	public List<Tag> query(String tag) {
		return this.repository.findTop10ByNameLike(tag);
	}
}
