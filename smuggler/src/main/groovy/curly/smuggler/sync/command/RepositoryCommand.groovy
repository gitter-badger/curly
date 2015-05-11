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
package curly.smuggler.sync.command

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import curly.commons.github.OctoUser
import curly.commons.logging.annotation.Loggable
import curly.smuggler.ExportedOctoRepository
import curly.smuggler.client.GitHubClient
import curly.smuggler.repository.ExportedOctoRepositoryRepository
import curly.smuggler.sync.Diff
import curly.smuggler.sync.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable
import rx.schedulers.Schedulers

import java.util.stream.Collectors
import java.util.stream.Stream

import static curly.commons.reactor.DispatcherFactory.ringBufferDispatcher

/**
 * @author Joao Pedro Evangelista
 */
@Service
class RepositoryCommand implements SyncCommand {

    @Autowired private ExportedOctoRepositoryRepository repository

    @Autowired private GitHubClient gitHubClient

    @Loggable
    @Override
    @HystrixCommand(fallbackMethod = "executeSyncFallback")
    DeferredResult<ResponseEntity<List<ExportedOctoRepository>>> executeSync(OctoUser octoUser) {

        def defer = new DeferredResult<ResponseEntity<List<ExportedOctoRepository>>>()

        def invoke = new Diff().invoke(
                getLocalRepositories(octoUser)
                        .toBlocking().first(),
                getRemoteRepositories(octoUser)
                        .toBlocking().first())

        Observable.just(invoke.get(Operation.REMOVE)).doOnNext({ repository.delete(it) })
                .subscribe { list ->
            list.each { l ->
                Observable.just(invoke.get(Operation.ADD))
                        .<List<ExportedOctoRepository>, List<ExportedOctoRepository>> zipWith(
                        Observable.just(invoke.get(Operation.KEEP)), { listAdd, listKeep ->
                    Stream.concat(listAdd.stream(), listKeep.stream()).collect(Collectors.toList())
                }).subscribe({ zipper ->
                    defer.setResult(ResponseEntity.ok(zipper))
                    zipper.each { z ->
                        exportFallback(z, l)
                    }
                }, { throwable ->
                    defer.setErrorResult(
                            new ResponseEntity<>(throwable, HttpStatus.INTERNAL_SERVER_ERROR))
                })
            }
        }
        return defer

    }

    private static void exportFallback(ExportedOctoRepository zipped, ExportedOctoRepository removable) {
        if (removable.octoRepository.id == zipped.octoRepository.id) {
            if (removable.exported) {
                zipped.exported = true
            }
        }
    }

    @Loggable
    @HystrixCommand
    private Observable<LinkedList<ExportedOctoRepository>> getRemoteRepositories(OctoUser octoUser) {
        return Observable.from(gitHubClient.getRepositories(octoUser),
                Schedulers.from(ringBufferDispatcher(RepositoryCommand)))
                .retry(2)
                .collect({ new LinkedList<ExportedOctoRepository>() }, { collector, items ->
            items.body.each { collector.add(new ExportedOctoRepository(it)) }
        })
    }

    @Loggable
    @HystrixCommand
    private Observable<List<ExportedOctoRepository>> getLocalRepositories(OctoUser octoUser) {
        Observable.from(repository.findByOctoRepositoryOwnerId(octoUser.id),
                Schedulers.from(ringBufferDispatcher(RepositoryCommand)))
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    private DeferredResult<List<ExportedOctoRepository>> executeSyncFallback(OctoUser octoUser) {
        def defer = new DeferredResult<List<ExportedOctoRepository>>()
        defer.setResult(repository.findByOctoRepositoryOwnerId(octoUser.id).get())
        return defer
    }
}
