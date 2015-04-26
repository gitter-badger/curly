/*
 * Copyright 2015 the original author or authors.
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
package curly.artifact

import curly.commons.logging.Loggable
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * @author Joao Pedro Evangelista
 * @since 19/04/2015
 */
@RepositoryRestResource(path = "arts")
interface ArtifactRepository extends MongoRepository<Artifact, String> {

    @Loggable
    @Cacheable("byLanguage")
    Page<Artifact> findByLanguages(String name, Pageable pageable)

    @Loggable
    @Cacheable("byType")
    Page<Artifact> findByTypes(String name, Pageable pageable)

    @Loggable
    @Cacheable("byCategory")
    Page<Artifact> findByCategory(String name, Pageable pageable)

    @Loggable
    @CacheEvict(["artifacts", "singleArtifact", "byLanguage", "byType", "byCategory"])
    Artifact save(Artifact artifact)

    @Loggable
    @CacheEvict(["artifacts", "singleArtifact", "byLanguage", "byType", "byCategory"])
    void delete(Artifact artifact)

    @Loggable
    @Cacheable("artifacts")
    Page<Artifact> findAll(Pageable pageable)

    @Loggable
    @Cacheable("singleArtifact")
    Artifact findOne(String id)
}
