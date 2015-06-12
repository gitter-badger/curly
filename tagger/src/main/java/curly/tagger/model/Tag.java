package curly.tagger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author João Evangelista
 */
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

	@Id
	private String id;

	@Indexed
	private String name;

}
