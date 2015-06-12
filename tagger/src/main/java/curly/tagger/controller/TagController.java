package curly.tagger.controller;

import com.google.common.base.Optional;
import curly.commons.logging.annotation.Loggable;
import curly.tagger.command.TagCommand;
import curly.tagger.model.SearchResult;
import curly.tagger.model.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.inject.Inject;
import javax.validation.Valid;

import static curly.commons.rx.RxResult.defer;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author João Evangelista
 */
@RestController
@RequestMapping("/tags")
class TagController {

	private final TagCommand tagCommand;

	@Inject
	public TagController(TagCommand tagCommand) {
		this.tagCommand = tagCommand;
	}


	@Loggable
	@RequestMapping(value = "/{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DeferredResult<ResponseEntity<Tag>> get(@PathVariable String tag) {
		return defer(tagCommand.get(tag)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(new ResponseEntity<>(NOT_FOUND)));
	}

	@Loggable
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DeferredResult<ResponseEntity<SearchResult>> search(@RequestParam(value = "q", defaultValue = "") String query) {
		return defer(tagCommand.like(query)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(SearchResult::new)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(new ResponseEntity<>(NOT_FOUND)));
	}

	@RequestMapping(method = RequestMethod.POST)
	public DeferredResult<ResponseEntity<?>> save(@Valid @RequestBody Tag tag) {
		tagCommand.save(tag);
		return defer(Observable.just(new ResponseEntity<>(CREATED)));
	}
}
