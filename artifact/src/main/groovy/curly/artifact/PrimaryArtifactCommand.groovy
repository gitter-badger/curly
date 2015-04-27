package curly.artifact

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.retry.annotation.Retryable
import rx.Observable

import javax.validation.constraints.NotNull

/**
 * Default implementation for Hystrix commands to wrap
 * executions on database which might fail
 *
 * @author João Pedro Evangelista
 */
class PrimaryArtifactCommand implements ArtifactCommand {

    private @Autowired
    @NotNull
    ArtifactRepository repository

    //Observable<Page<Artifact>>
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultArtifacts")
    Observable<Optional<Page<Artifact>>> artifacts(Pageable pageable) {
        return new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.ofNullable(repository.findAll(pageable))
            }
        }
    }
//Observable<Artifact>
    @Override
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultArtifact")
    Observable<Optional<Artifact>> artifact(String id) {
        new ObservableResult() {
            @Override
            def Artifact invoke() {
                Optional.ofNullable repository.findOne(id)
            }
        }
    }

    @Override
    @Retryable
    def void delete(Artifact artifact) {
        repository.delete(artifact)
    }

    @Override
    @Retryable
    Observable<Optional<Artifact>> save(Artifact artifact) {
        if (artifact.id == null) artifact.id = ObjectId.get().toHexString()
        Observable.just(Optional.ofNullable(repository.save(artifact)))
    }

}
