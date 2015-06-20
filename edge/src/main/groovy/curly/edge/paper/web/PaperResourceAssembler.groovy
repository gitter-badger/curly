package curly.edge.paper.web

import curly.edge.paper.domain.Paper
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

/**
 * @author Joao Pedro Evangelista
 */
@Component
class PaperResourceAssembler extends ResourceAssemblerSupport<Paper, PaperResource> {
    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType must not be {@literal null}.
     */
    PaperResourceAssembler() {
        super(PaperController, PaperResource)
    }

    @Override
    PaperResource toResource(Paper entity) {
        new PaperResource(entity, ControllerLinkBuilder.linkTo(PaperController).slash(entity.id).withSelfRel())
    }
}
