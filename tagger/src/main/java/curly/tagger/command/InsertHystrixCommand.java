package curly.tagger.command;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import curly.commons.logging.annotation.Loggable;
import curly.tagger.model.Tag;
import curly.tagger.service.TagService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author João Evangelista
 */
@Service("insertCommand")
public class InsertHystrixCommand implements InsertCommand {

	private final TagService tagService;

	@Inject
	public InsertHystrixCommand(@NotNull TagService tagService) {
		this.tagService = tagService;
	}

	@Override
	@Loggable
	@Retryable
	@HystrixCommand
	public void save(Tag tag) {
		tagService.save(tag);
	}

	public TagService getTagService() {
		return tagService;
	}
}
