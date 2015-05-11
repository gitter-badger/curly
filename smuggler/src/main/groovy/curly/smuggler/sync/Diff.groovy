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
package curly.smuggler.sync
import curly.commons.logging.annotation.Loggable
import curly.smuggler.ExportedOctoRepository
import groovy.transform.Immutable
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
/**
 * @author Joao Pedro Evangelista
 */
@Immutable
class Diff implements SyncOperation<ExportedOctoRepository>, Serializable {

    @Loggable
    @Override
    public MultiValueMap<Operation, ExportedOctoRepository> invoke(List<ExportedOctoRepository> repositories, List<ExportedOctoRepository> gitHubRepositories) {
        MultiValueMap<Operation, ExportedOctoRepository> multiValueMap = new LinkedMultiValueMap<>(0)
        repositories.each { repo ->
            if (gitHubRepositories.contains(repo)) {
                multiValueMap.add Operation.KEEP, repo
            } else {
                multiValueMap.add Operation.REMOVE, repo
            }
        }
        gitHubRepositories.each { req ->
            if (!repositories.contains(req)) {
                multiValueMap.add Operation.ADD, req
            }
        }
        return multiValueMap
    }


}
