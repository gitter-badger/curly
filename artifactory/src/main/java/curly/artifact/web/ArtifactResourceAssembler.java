package curly.artifact.web;

import curly.artifact.model.Artifact;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author João Evangelista
 */
@Component
class ArtifactResourceAssembler extends ResourceAssemblerSupport<Artifact, ArtifactResource> {

	public ArtifactResourceAssembler() {
		super(ArtifactResourceController.class, ArtifactResource.class);
	}


	@Override
	public ArtifactResource toResource(Artifact entity) {
		return new ArtifactResource(entity,
				linkTo(ArtifactResourceController.class)
						.slash(entity.getId())
						.withSelfRel());
	}
}
