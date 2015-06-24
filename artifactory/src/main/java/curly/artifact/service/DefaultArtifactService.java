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
import curly.commons.github.OctoUser;
import curly.commons.logging.annotation.Loggable;
import curly.commons.security.negotiation.ResourceOperationsResolverAdapter;
import curly.commons.stereotype.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.Assert;

/**
 * @author João Pedro Evangelista
 */
@Slf4j
@Command
@Retryable
public class DefaultArtifactService extends ResourceOperationsResolverAdapter<Artifact, OctoUser> implements ArtifactService {

	private final ArtifactRepository repository;


	@Autowired
	public DefaultArtifactService(ArtifactRepository artifactRepository) {
		this.repository = artifactRepository;
	}


	@Loggable @Override @Cacheable("artifacts")
	public Page<Artifact> findAll(Pageable pageable) {
		Assert.notNull(pageable, "Page information must be not null!");
		log.debug("Finding for page {}", pageable.getPageNumber());
		return repository.findAll(pageable);
	}

	@Loggable @Override @CachePut(value = {"artifacts", "singleArtifact"}, key = "#artifact.id")
	public Artifact save(Artifact artifact, OctoUser octoUser) {
		Assert.notNull(artifact, "Artifact must be not null1");
		Assert.notNull(octoUser);
		checkMatch(artifact, octoUser);
		return repository.save(artifact);
	}


	@Loggable @Override @Cacheable(value = "singleArtifact", key = "#id")
	public Artifact findOne(String id) {
		Assert.hasText(id, "Id must be not null or empty!");
		return repository.findOne(id);
	}

	@Override @Loggable @CacheEvict(value = {"artifacts", "singleArtifact"}, key = "#id")
	public void delete(String id, OctoUser user) {
		Assert.notNull(user, "OctoUser must be not null");
		Assert.hasText(id, "Id must be not empty");
		log.debug("Looking for entity with id {}", id);
		Artifact artifact = findOne(id);
		if (artifact != null && isOwnedBy(artifact, user)) {
			repository.delete(artifact);
		} else {
			log.error("Cannot process #delete({},{})", id, user.getId());
		}
	}

}
