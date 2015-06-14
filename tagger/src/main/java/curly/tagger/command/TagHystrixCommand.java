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

import com.google.common.base.Optional;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import curly.commons.logging.annotation.Loggable;
import curly.commons.stereotype.Command;
import curly.tagger.model.Tag;
import curly.tagger.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import rx.Observable;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.google.common.base.Optional.fromNullable;

/**
 * @author João Evangelista
 */
@Slf4j
@Command
public class TagHystrixCommand extends OperationsHystrixCommand implements TagCommand {

	private final TagService tagService;

	@Inject
	public TagHystrixCommand(@NotNull TagService tagService) {
		super(tagService);
		this.tagService = getTagService();
	}


	@Override
	@Loggable
	@Retryable(maxAttempts = 1)
	@HystrixCommand
	public Observable<Optional<Tag>> get(final String tag) {
		return new ObservableResult<Optional<Tag>>() {
			@Override
			public Optional<Tag> invoke() {
				return fromNullable(tagService.find(tag));
			}
		};
	}


	@Override
	@Loggable
	@HystrixCommand
	@Retryable(maxAttempts = 1)
	public Observable<Optional<List<Tag>>> like(final String tag) {
		return new ObservableResult<Optional<List<Tag>>>() {
			@Override
			public Optional<List<Tag>> invoke() {
				return fromNullable(tagService.query(tag));
			}
		};
	}

}
