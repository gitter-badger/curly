package curly.edge.paper.web

import curly.edge.paper.domain.Paper
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource

/**
 * @author Joao Pedro Evangelista
 */
class PaperResource extends Resource<Paper> {
    PaperResource(Paper content, Link... links) {
        super(content, links)
    }
}
