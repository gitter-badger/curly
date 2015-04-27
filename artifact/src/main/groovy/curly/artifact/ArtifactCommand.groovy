package curly.artifact

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import rx.Observable

/**
 * @author João Pedro Evangelista
 */
interface ArtifactCommand {
    Observable<Optional<Page<Artifact>>> artifacts(Pageable pageable)

    Observable<Optional<Artifact>> artifact(String id)

    Observable<Optional<Artifact>> save(Artifact artifact)

    void delete(Artifact artifact)

}
