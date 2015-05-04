/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
