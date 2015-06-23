package curly.paperclip.paper.web

import curly.paperclip.paper.model.Paper
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

/**
 * @author João Evangelista
 */
@Component
class PaperResourceAssembler extends ResourceAssemblerSupport<Paper, PaperResource> {

    PaperResourceAssembler() {
        super(PaperResourceController, PaperResource)
    }

    @Override
    PaperResource toResource(Paper entity) {
        return new PaperResource(entity, linkTo(PaperResourceController).slash(entity.item).withSelfRel())
    }
}
