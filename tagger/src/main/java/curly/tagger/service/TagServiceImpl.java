package curly.tagger.service;

import curly.tagger.model.Tag;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author João Evangelista
 */
@Service
class TagServiceImpl implements TagService {

	private final TagRepository repository;
	private final MongoTemplate mongoTemplate;

	@Inject
	TagServiceImpl(TagRepository repository, MongoTemplate mongoTemplate) {
		this.repository = repository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void save(Tag tag) {
		this.repository.save(tag);
	}

	@Override
	public Tag find(String tag) {
		return this.repository.findByName(tag);
	}

	@Override
	public List<Tag> query(String tag) {
		return this.repository.findTop10ByNameLike(tag);
	}
}
