package curly.artifact;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * @author João Pedro Evangelista
 */
public class ArtifactResource extends Resource<Artifact> {
    public ArtifactResource(Artifact content, Link... links) {
        super(content, links);
    }

    public ArtifactResource(Artifact content, Iterable<Link> links) {
        super(content, links);
    }
}
