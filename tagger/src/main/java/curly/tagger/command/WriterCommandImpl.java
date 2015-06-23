/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package curly.tagger.command;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import curly.commons.logging.annotation.Loggable;
import curly.tagger.model.Tag;
import curly.tagger.service.TagService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author João Evangelista
 */
@Service("insertCommand")
public class WriterCommandImpl implements WriterCommand {

	private final TagService tagService;

	@Inject
	public WriterCommandImpl(@NotNull TagService tagService) {
		this.tagService = tagService;
	}

	@Override
	@Loggable
	@Retryable
	@HystrixCommand
	public void save(Set<Tag> tags) {
		tagService.save(tags);
	}

	public TagService getTagService() {
		return tagService;
	}
}
