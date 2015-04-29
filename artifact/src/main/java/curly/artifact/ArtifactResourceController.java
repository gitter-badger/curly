package curly.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

import static curly.commons.rx.RxResult.defer;

/**
 * @author João Pedro Evangelista
 */
@RestController
@RequestMapping("/arts")
public class ArtifactResourceController {

    private final ArtifactService artifactService;


    @Autowired
    public ArtifactResourceController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<List<Artifact>> artifactResources(@PageableDefault(20) Pageable pageable) {
        System.out.println(pageable.getPageNumber());
        return defer(this.artifactService.findAll(pageable)
                .map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
                .map(Slice::getContent));

    }
}
