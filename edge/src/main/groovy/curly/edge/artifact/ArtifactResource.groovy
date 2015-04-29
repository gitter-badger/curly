package curly.edge.artifact

import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource

/**
 * @author João Pedro Evangelista
 */
class ArtifactResource extends Resource<Artifact> {
    ArtifactResource(Artifact content, Link... links) {
        super(content, links)
    }
}
