package curly.bloodhound.model;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author João Evangelista
 */
public interface ArtifactRepository extends ElasticsearchRepository<Artifact, String> {

}
