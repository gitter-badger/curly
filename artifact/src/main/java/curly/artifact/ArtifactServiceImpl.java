package curly.artifact;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author João Pedro Evangelista
 */
@Service
public class ArtifactServiceImpl implements ArtifactService {

    private final ArtifactRepository artifactRepository;

    @Autowired
    public ArtifactServiceImpl(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    @HystrixCommand
    @Override public Future<Page<Artifact>> findAll(Pageable pageable) {
        return new AsyncResult<Page<Artifact>>() {
            @Override public Page<Artifact> invoke() {
                return artifactRepository.findAll(pageable);
            }
        };
    }

}
