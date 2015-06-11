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
package curly.artifact.service;

import curly.artifact.model.Artifact;
import curly.commons.logging.annotation.Loggable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Joao Pedro Evangelista
 * @since 19/04/2015
 */
@RepositoryRestResource(exported = true)
interface ArtifactRepository extends MongoRepository<Artifact, String> {

	@Loggable
	Page<Artifact> findByLanguages(String name, Pageable pageable);

	@Loggable
	Page<Artifact> findByTypes(String name, Pageable pageable);

	@Loggable
	Page<Artifact> findByCategory(String name, Pageable pageable);

	@Override
	@SuppressWarnings("unchecked")
	@Loggable
	@CachePut(value = {"artifacts", "singleArtifact"})
	Artifact save(Artifact artifact);

	@Override
	@Loggable
	@Cacheable(value = "singleArtifact")
	Artifact findOne(String id);

	@Override
	@Loggable
	@CacheEvict(value = {"artifacts", "singleArtifact"})
	void delete(Artifact artifact);

	@Override
	@Loggable
	@Cacheable(value = "artifacts")
	Page<Artifact> findAll(Pageable pageable);
}
