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
package curly.artifact

import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

/**
 * @author João Pedro Evangelista
 */
@Component
class ArtifactResourceAssembler extends ResourceAssemblerSupport<Artifact, Resource> {

    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     */
    ArtifactResourceAssembler() {
        super(ArtifactResourceController, Resource)
    }

    /**
     * @see ResourceAssemblerSupport#toResources(java.lang.Iterable)
     * @param entity a single entity to build
     * @return resource built with reference to id
     */
    @Override
    Resource toResource(Artifact entity) {
        new Resource<Artifact>(
                entity, linkTo(ArtifactResourceController)
                .slash(entity.id).withSelfRel()
        )
    }

}
