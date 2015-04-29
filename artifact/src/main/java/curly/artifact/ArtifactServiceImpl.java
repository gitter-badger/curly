package curly.artifact;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.Optional;

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
    @Override public Observable<Optional<Page<Artifact>>> findAll(Pageable pageable) {
        return new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            public Optional<Page<Artifact>> invoke() {
                return Optional.ofNullable(artifactRepository.findAll(pageable));
            }
        };
    }

}
