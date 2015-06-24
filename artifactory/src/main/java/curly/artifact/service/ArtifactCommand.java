package curly.artifact.service;

import curly.artifact.model.Artifact;
import curly.commons.github.OctoUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rx.Observable;

import java.util.Optional;

/**
 * @author João Evangelista
 */
public interface ArtifactCommand {

	Observable<Optional<Page<Artifact>>> findAllByPage(Pageable pageable);


	Observable<Optional<Artifact>> save(Artifact artifact, OctoUser octoUser);

	Observable<Optional<Artifact>> findOne(String id);

	void delete(String id, OctoUser octoUser);
}
