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
package curly.merchant.sync.command

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import curly.commons.config.feign.ex.InternalServerErrorException
import curly.commons.github.OctoUser
import curly.commons.logging.annotation.Loggable
import curly.merchant.client.GitHubClient
import curly.merchant.domain.ExportedOctoRepository
import curly.merchant.repository.ExportedOctoRepositoryRepository
import curly.merchant.sync.Diff
import curly.merchant.sync.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.context.request.async.DeferredResult

import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author Joao Pedro Evangelista
 */
@Service
class RepositoryCommand implements SyncCommand {

    @Autowired
    private ExportedOctoRepositoryRepository repository

    @Autowired
    private GitHubClient gitHubClient

    @Loggable
    @Override
    @HystrixCommand(fallbackMethod = "executeSyncFallback")
    DeferredResult<ResponseEntity<List<ExportedOctoRepository>>> executeSync(OctoUser octoUser) {

        def defer = new DeferredResult<ResponseEntity<List<ExportedOctoRepository>>>()

        def invoke = new Diff().invoke(
                getLocalRepositories(octoUser),
                getRemoteRepositories(octoUser)
        )

        def result = repository.save(exportFallback(
                Stream.concat(
                        invoke.get(Operation.ADD).stream(),
                        invoke.get(Operation.KEEP).stream())
                        .collect(Collectors.toList()),
                invoke.get(Operation.REMOVE).each { repository.delete(it) }))
                .toList()
        defer.setResult(ResponseEntity.ok(result))
        return defer

    }

    static List<ExportedOctoRepository> exportFallback(List<ExportedOctoRepository> zipped, List<ExportedOctoRepository> removable) {
        List<ExportedOctoRepository> list = new LinkedList<>()
        removable.each { r ->
            zipped.each { zip ->
                if (r.octoRepository.id == zip.octoRepository.id) {
                    if (r.exported) {
                        zip.exported = true
                        list.add zip
                    }
                }
            }
        }
        Stream.concat(list.stream(), zipped.stream()).collect(Collectors.toList())
    }


    @Loggable
    private LinkedList<ExportedOctoRepository> getRemoteRepositories(OctoUser octoUser) {
        List<ExportedOctoRepository> exportedOctoRepositoryList = new LinkedList<>()
        gitHubClient.getRepositories(octoUser).addCallback({
            it.body.each { exportedOctoRepositoryList.add(new ExportedOctoRepository(it)) }
        }, {
            throw new InternalServerErrorException("Could not process rest call", it)
        })
        exportedOctoRepositoryList
    }


    @Loggable
    private List<ExportedOctoRepository> getLocalRepositories(OctoUser octoUser) {
        repository.findByOctoRepositoryOwnerId(octoUser.id)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    private DeferredResult<ResponseEntity<List<ExportedOctoRepository>>> executeSyncFallback(OctoUser octoUser) {
        def defer = new DeferredResult<ResponseEntity<List<ExportedOctoRepository>>>()
        defer.setResult(ResponseEntity.ok(repository.findByOctoRepositoryOwnerId(octoUser.id)))
        return defer
    }

}
