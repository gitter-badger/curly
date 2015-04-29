package curly.artifact;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.Collection;

/**
 * @author João Pedro Evangelista
 */
public class PagedArtifact extends PagedResources<Artifact> {
    protected PagedArtifact() {
        super();
    }

    public PagedArtifact(Collection<Artifact> content, PageMetadata metadata, Link... links) {
        super(content, metadata, links);
    }

    public PagedArtifact(Page<Artifact> artifacts) {
        super(artifacts.getContent(), new PagedResources.PageMetadata(artifacts.getSize(), artifacts.getNumber(), artifacts.getTotalElements(), artifacts.getTotalPages()));
    }
}
