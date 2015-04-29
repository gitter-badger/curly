package curly.artifact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.Future;

/**
 * @author João Pedro Evangelista
 */
public interface ArtifactService {
    Future<Page<Artifact>> findAll(Pageable pageable);
}
