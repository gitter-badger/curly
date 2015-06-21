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
package curly.artifact.controller;

import curly.artifact.model.Artifact;
import curly.artifact.model.PagedArtifact;
import curly.artifact.service.ArtifactService;
import curly.commons.github.GitHubAuthentication;
import curly.commons.github.OctoUser;
import curly.commons.rx.RxResult;
import curly.commons.web.BadRequestException;
import curly.commons.web.ModelErrors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.validation.Valid;

import static curly.commons.rx.RxResult.defer;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author João Pedro Evangelista
 */
@Slf4j
@RestController
@RequestMapping("/arts")
class ArtifactResourceController {

	private final ArtifactService artifactService;

	@Autowired
	ArtifactResourceController(ArtifactService artifactService) {
		this.artifactService = artifactService;
	}

	@RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DeferredResult<HttpEntity<PagedArtifact>> artifactResources(@PageableDefault(20) Pageable pageable) {
		log.trace("Querying resources with page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
		artifactService.findAll(pageable);
		return defer(
				artifactService.findAll(pageable)
						.map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
						.map(PagedArtifact::new)
						.map(ResponseEntity::ok)
		);
	}

	@RequestMapping(
			value = "/{id}",
			method = GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public DeferredResult<HttpEntity<Artifact>> artifactResource(@PathVariable("id") String id) {
		log.trace("Querying single resource based on id {}", id);
		if (!ObjectId.isValid(id)) throw new BadRequestException("Provided id " + id + " is not valid!");

		return defer(
				artifactService.findOne(id)
						.map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
						.map(ResponseEntity::ok));
	}


	@RequestMapping(method = {POST, PUT})
	public DeferredResult<HttpEntity<?>> saveResource(@Valid @RequestBody Artifact artifact,
													  @GitHubAuthentication OctoUser octoUser,
													  BindingResult bindingResult) {
		log.debug("Performing save operations based on user {}", octoUser.getId());
		if (bindingResult.hasErrors()) {
			return RxResult.defer(Observable.just(new ResponseEntity<>(new ModelErrors(BAD_REQUEST, bindingResult), BAD_REQUEST)));
		}
		artifactService.save(artifact, octoUser);
		return RxResult.defer(Observable.just(new ResponseEntity<>(CREATED)));
	}

	@RequestMapping(
			value = "/{id}",
			method = DELETE)
	public DeferredResult<HttpEntity<?>> deleteResource(@PathVariable("id") String id,
														@GitHubAuthentication OctoUser octoUser) {
		log.trace("Performing deletion operations based on user {}", octoUser.getId());
		if (!ObjectId.isValid(id)) throw new BadRequestException("Provided id " + id + " is not valid!");

		artifactService.delete(id, octoUser);
		return RxResult.defer(Observable.just(new ResponseEntity<>(NO_CONTENT)));
	}

}
