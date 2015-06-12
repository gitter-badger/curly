package curly.tagger.command;

import com.google.common.base.Optional;
import curly.tagger.model.Tag;
import rx.Observable;

import java.util.List;

/**
 * @author João Evangelista
 */
public interface TagCommand {
	Observable<Optional<Tag>> get(String tag);

	Observable<Optional<List<Tag>>> like(String tag);

	void save(Tag tag);
}
