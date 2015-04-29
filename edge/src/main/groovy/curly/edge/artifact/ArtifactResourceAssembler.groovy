package curly.edge.artifact

import curly.EdgeController
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

/**
 * @author João Pedro Evangelista
 */
@Component
class ArtifactResourceAssembler extends ResourceAssemblerSupport<Artifact, ArtifactResource> {
    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     *
     */
    ArtifactResourceAssembler() {
        super(EdgeController, ArtifactResource)
    }

    @Override
    ArtifactResource toResource(Artifact entity) {
        return new ArtifactResource(
                entity, ControllerLinkBuilder.linkTo(EdgeController).slash(entity.id).withSelfRel()
        )
    }
}
