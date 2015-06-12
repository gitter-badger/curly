package curly.tagger.service;

import curly.tagger.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author João Evangelista
 */
interface TagRepository extends MongoRepository<Tag, String> {

	Tag findByName(String name);

	List<Tag> findTop10ByNameLike(String tag);
}
