package curly;

import curly.commons.rx.RxResult;
import curly.commons.web.hateoas.PageProcessor;
import curly.edge.artifact.Artifact;
import curly.edge.artifact.ArtifactResource;
import curly.edge.artifact.ArtifactResourceAssembler;
import curly.edge.artifact.repository.ArtifactClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author João Pedro Evangelista
 */
public class EdgeControllers {

    @Autowired ArtifactClient client;

    DeferredResult<ResponseEntity<PagedResources<ArtifactResource>>> hello(@PageableDefault(20) Pageable pageable, PagedResourcesAssembler<Artifact> assembler) {
        return RxResult.defer(rx.Observable.just(client.findAll(pageable.getPageNumber(), pageable.getPageSize()))
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .map(e -> assembler.toResource(PageProcessor.toPage(e.getBody()), new ArtifactResourceAssembler()))
                .map(ResponseEntity::ok));

    }
}
