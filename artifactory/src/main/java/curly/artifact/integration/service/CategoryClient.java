package curly.artifact.integration.service;

import curly.artifact.model.Category;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author João Evangelista
 */
@FeignClient("formula")
public interface CategoryClient {

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	ResponseEntity<?> postEvent(@RequestBody Category categories);
}
