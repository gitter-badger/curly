package curly.paperclip.paper.web

import curly.paperclip.paper.model.Paper
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource

/**
 * @author João Evangelista
 */
class PaperResource extends Resource<Paper> {
    PaperResource(Paper content, Link... links) {
        super(content, links)
    }
}
