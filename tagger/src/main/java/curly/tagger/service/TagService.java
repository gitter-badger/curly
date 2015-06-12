package curly.tagger.service;

import curly.tagger.model.Tag;

import java.util.List;

/**
 * @author João Evangelista
 */
public interface TagService {

	void save(Tag tag);

	Tag find(String tag);

	List<Tag> query(String tag);
}
