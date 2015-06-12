package curly.tagger.model;

import lombok.Data;

import java.util.List;

/**
 * @author João Evangelista
 */
@Data
public class SearchResult {

	private final List<Tag> result;

	private final int size;

	public SearchResult(List<Tag> result) {
		this.result = result;
		size = result.size();
	}
}
