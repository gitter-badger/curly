package curly.edge.artifact.command

import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.edge.artifact.Artifact
import curly.edge.artifact.repository.ArtifactClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert

import javax.validation.constraints.NotNull
/**
 * @author João Pedro Evangelista
 */
@Service
class PrimaryArtifactCommand implements ArtifactCommand {

    private final ArtifactClient artifactClient

    @Autowired
    PrimaryArtifactCommand(@NotNull ArtifactClient artifactClient) {
        this.artifactClient = artifactClient
    }

    @Override
    def artifacts(Integer page, Integer size) {

        Assert.notNull(page, "Artifact page must not be null")
        Assert.notNull(size, "Artifact size must not be null")

        new ObservableResult() {
            @Override
            def invoke() {
                artifactClient.findAll(page, size)
            }
        }
    }

    @Override
    def artifact(String id) {
        Assert.notNull(id, "Artifact id must not be null")
        new ObservableResult() {
            @Override
            def invoke() {
                artifactClient.findOne(id)
            }
        }
    }

    @Override
    def delete(String id) {
        Assert.notNull(id, "Artifact id for delete must not be null")
        artifactClient.delete(id)
    }

    @Override
    def save(Artifact artifact) {
        Assert.notNull(id, "Artifact must not be null")
        artifactClient.save(artifact)
    }


}
