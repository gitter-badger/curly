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
package curly.artifact.web;

import curly.artifact.model.Artifact;
import curly.artifact.service.ArtifactService;
import curly.commons.github.GitHubAuthentication;
import curly.commons.github.OctoUser;
import curly.commons.web.BadRequestException;
import curly.commons.web.ModelErrors;
import curly.commons.web.hateoas.MediaTypes;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
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
@RequestMapping("/artifacts")
class ArtifactResourceController {

	private final ArtifactService artifactService;

	private final ArtifactResourceAssembler resourceAssembler;

	@Autowired
	ArtifactResourceController(ArtifactService artifactService, ArtifactResourceAssembler resourceAssembler) {
		this.artifactService = artifactService;
		this.resourceAssembler = resourceAssembler;
	}

	/**
	 * Process a page of Artifacts into a HAL representation or return 404 if not found or was null value
	 *
	 * @param pageable  acquired from url parameters
	 * @param assembler inject by Spring
	 * @return Observable of a HttpEntity of a Paged Resources of Artifacts
	 */
	@RequestMapping(method = GET, produces = MediaTypes.HAL_JSON)
	public Observable<HttpEntity<PagedResources<ArtifactResource>>> artifactResources(@NotNull @PageableDefault(20) Pageable pageable,
																					  @NotNull PagedResourcesAssembler<Artifact> assembler) {

		return artifactService.findAll(pageable)
				.map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
				.map(artifacts -> assembler.toResource(artifacts, resourceAssembler))
				.map(ResponseEntity::ok);
	}

	@RequestMapping(value = "/{id}", method = GET, produces = MediaTypes.HAL_JSON)
	public DeferredResult<HttpEntity<ArtifactResource>> artifactResource(@PathVariable("id") String id) {

		if (!ObjectId.isValid(id)) throw new BadRequestException("Provided id " + id + " is not valid!");

		return defer(artifactService.findOne(id)
				.map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
				.map(resourceAssembler::toResource)
				.map(ResponseEntity::ok));
	}


	@RequestMapping(method = {POST, PUT})
	public DeferredResult<HttpEntity<?>> saveResource(@Valid @RequestBody Artifact artifact,
													  @NotNull @GitHubAuthentication OctoUser octoUser,
													  @NotNull BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return defer(Observable.just(new ResponseEntity<>(new ModelErrors(bindingResult), BAD_REQUEST)));
		}

		artifactService.save(artifact, octoUser);
		return defer(Observable.just(new ResponseEntity<>(CREATED)));
	}

	@RequestMapping(
			value = "/{id}",
			method = DELETE)
	public DeferredResult<HttpEntity<?>> deleteResource(@PathVariable("id") String id,
														@NotNull @GitHubAuthentication OctoUser octoUser) {

		if (!ObjectId.isValid(id)) throw new BadRequestException("Provided id " + id + " is not valid!");

		artifactService.delete(id, octoUser);
		return defer(Observable.just(new ResponseEntity<>(NO_CONTENT)));
	}

}
