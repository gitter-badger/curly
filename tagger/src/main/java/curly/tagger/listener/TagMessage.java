package curly.tagger.listener;

import curly.tagger.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author João Evangelista
 */
@Data
@NoArgsConstructor
public class TagMessage {

	private String id;
	private String name;

	public TagMessage(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public static TagMessage from(Tag tag) {
		return new TagMessage(tag.getId(), tag.getName());
	}

	public Tag toTag() {
		return new Tag(this.id, this.name);
	}
}
