package curly.artifact;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author João Pedro Evangelista
 */
@Component
public class ArtifactResourceAssembler extends ResourceAssemblerSupport<Artifact, ArtifactResource> {


    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     */
    public ArtifactResourceAssembler() {
        super(ArtifactResourceController.class, ArtifactResource.class);
    }

    @Override public ArtifactResource toResource(Artifact entity) {
        return new ArtifactResource(entity, linkTo(ArtifactResourceController.class).slash(entity.getId()).withSelfRel());
    }
}
