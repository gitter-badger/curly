package curly.edge.artifact.command

import curly.edge.artifact.Artifact

/**
 * @author João Pedro Evangelista
 */
interface ArtifactCommand {

    def artifacts(Integer page, Integer size)

    def artifact(String id)

    def delete(String id)

    def save(Artifact artifact)

}
