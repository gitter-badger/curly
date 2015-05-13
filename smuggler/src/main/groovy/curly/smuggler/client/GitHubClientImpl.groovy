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
package curly.smuggler.client

import curly.commons.github.OctoUser
import curly.commons.logging.annotation.Loggable
import curly.smuggler.OctoRepository
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.client.AsyncRestTemplate

/**
 * @author Joao Pedro Evangelista
 */
@Component
class GitHubClientImpl implements GitHubClient {

    final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate()

    static
    final ParameterizedTypeReference<List<OctoRepository>> reference = new ParameterizedTypeReference<List<OctoRepository>>() {
    }

    @Loggable
    @Override
    ListenableFuture<ResponseEntity<List<OctoRepository>>> getRepositories(OctoUser octoUser) {
        asyncRestTemplate.exchange("https://api.github.com/users/{user}/repos", HttpMethod.GET, null, reference, octoUser.login)

    }

}
