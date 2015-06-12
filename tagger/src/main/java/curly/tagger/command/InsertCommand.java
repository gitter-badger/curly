package curly.tagger.command;

import curly.tagger.model.Tag;

/**
 * @author João Evangelista
 */
public interface InsertCommand {

	void save(Tag tag);
}
