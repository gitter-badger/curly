package curly.artifact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rx.Observable;

import java.util.Optional;

/**
 * @author João Pedro Evangelista
 */
public interface ArtifactService {
    Observable<Optional<Page<Artifact>>> findAll(Pageable pageable);
}
