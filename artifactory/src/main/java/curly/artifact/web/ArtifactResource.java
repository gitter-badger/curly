package curly.artifact.web;

import curly.artifact.model.Artifact;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * @author João Evangelista
 */
public final class ArtifactResource extends Resource<Artifact> {

	public ArtifactResource(Artifact content, Link... links) {
		super(content, links);
	}
}
