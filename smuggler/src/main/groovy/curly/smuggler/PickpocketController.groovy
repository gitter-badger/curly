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
package curly.smuggler
import curly.commons.github.GitHubAuthentication
import curly.commons.github.OctoUser
import curly.smuggler.sync.command.SyncCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.context.request.async.DeferredResult
/**
 * @author Joao Pedro Evangelista
 */
@Controller
@RequestMapping("/pickpocket")
class PickpocketController {

    @Autowired private SyncCommand command

    @RequestMapping(method = RequestMethod.PATCH)
    DeferredResult<ResponseEntity<List<ExportedOctoRepository>>> repositoriesSync(
            @GitHubAuthentication OctoUser octoUser) {
        return command.executeSync(octoUser)
    }

}
