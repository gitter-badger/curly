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
package curly.edge.artifact

import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.ResourceAssembler
import org.springframework.hateoas.mvc.ControllerLinkBuilder

/**
 * @author João Pedro Evangelista
 */
class PagedArtifact extends PagedResources<Artifact> implements ResourceAssembler<Artifact, ArtifactResource> {

    protected PagedArtifact() {
        super()
    }

    @SuppressWarnings("UnnecessaryQualifiedReference")
    PagedArtifact(Collection<Artifact> content, PagedResources.PageMetadata metadata, Link... links) {
        super(content, metadata, links)
    }

    @SuppressWarnings("UnnecessaryQualifiedReference")
    PagedArtifact(Collection<Artifact> content, PagedResources.PageMetadata metadata, Iterable<Link> links) {
        super(content, metadata, links)
    }

    @Override
    ArtifactResource toResource(Artifact entity) {
        return new ArtifactResource(entity,
                ControllerLinkBuilder.linkTo(ArtifactController)
                        .slash(entity.id).withSelfRel())
    }
}
