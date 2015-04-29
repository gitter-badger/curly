package curly.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

/**
 * @author João Pedro Evangelista
 */
@RestController
@RequestMapping("/arts")
public class ArtifactResourceController {

    private final ArtifactService artifactService;

    private final ArtifactResourceAssembler assembler;

    @Autowired
    public ArtifactResourceController(ArtifactService artifactService, ArtifactResourceAssembler assembler) {
        this.artifactService = artifactService;
        this.assembler = assembler;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<PagedResources<ArtifactResource>> artifactResources(@PageableDefault(20) Pageable pageable,
                                                                              PagedResourcesAssembler<Artifact> pagedResourcesAssembler) {
        System.out.println(pageable.getPageNumber());
        Observable<PagedResources<ArtifactResource>> pagedResourcesObservable = Observable.from(artifactService.findAll(pageable)).map(a -> pagedResourcesAssembler.toResource(a, assembler));

        return result(pagedResourcesObservable);
    }

    private <T> DeferredResult<T> result(Observable<T> t) {
        DeferredResult<T> result = new DeferredResult<>();
        t.subscribe(result::setResult, result::setErrorResult);
        //result.setResult(t);
        return result;

    }
}
