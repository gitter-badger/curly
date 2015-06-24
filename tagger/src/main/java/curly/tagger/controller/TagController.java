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
package curly.tagger.controller;

import com.google.common.base.Optional;
import curly.commons.logging.annotation.Loggable;
import curly.commons.web.HttpHeaders;
import curly.commons.web.ModelErrors;
import curly.tagger.command.ReaderCommand;
import curly.tagger.command.WriterCommand;
import curly.tagger.model.SearchResult;
import curly.tagger.model.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Set;

import static curly.commons.rx.RxResult.defer;
import static org.springframework.http.HttpStatus.*;

/**
 * @author João Evangelista
 */
@RestController
@RequestMapping("/tags")
class TagController {

	private final ReaderCommand readerCommand;

	private final WriterCommand writerCommand;

	@Inject
	public TagController(ReaderCommand readerCommand, WriterCommand writerCommand) {
		this.readerCommand = readerCommand;
		this.writerCommand = writerCommand;
	}

	@Loggable
	@RequestMapping(value = "/{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = HttpHeaders.INTERNAL_V1)
	public DeferredResult<ResponseEntity<Tag>> get(@PathVariable String tag) {
		return defer(readerCommand.get(tag)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(new ResponseEntity<>(NOT_FOUND)));
	}

	@Loggable
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = HttpHeaders.INTERNAL_V1)
	public DeferredResult<ResponseEntity<SearchResult>> search(@RequestParam(value = "q", defaultValue = "") String query) {
		return defer(readerCommand.like(query)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(SearchResult::new)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(new ResponseEntity<>(NOT_FOUND)));
	}

	@RequestMapping(method = RequestMethod.POST, headers = HttpHeaders.INTERNAL_V1)
	public DeferredResult<ResponseEntity<?>> save(@Valid @RequestBody Set<Tag> tags, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return defer(Observable.just(new ResponseEntity<>(new ModelErrors(bindingResult), BAD_REQUEST)));

		writerCommand.save(tags);

		return defer(Observable.just(new ResponseEntity<>(CREATED)));
	}
}
