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
package curly.paperclip.paper.command

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.commons.config.feign.ex.UnauthorizedException
import curly.commons.github.OctoUser
import curly.commons.logging.annotation.Loggable
import curly.commons.security.negotiation.ResourceOperationsResolverAdapter
import curly.paperclip.paper.model.Paper
import curly.paperclip.paper.service.PaperService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import reactor.core.support.Assert
import rx.Observable

import javax.inject.Inject

import static java.util.Optional.ofNullable

/**
 * @author Joao Pedro Evangelista
 */
@Service
@Retryable(exclude = [IllegalStateException])
class PaperCommandImpl extends ResourceOperationsResolverAdapter<Paper, OctoUser> implements PaperCommand {

    private PaperService service

    private static final Logger LOGGER = LoggerFactory.getLogger(PaperCommandImpl)

    @Inject
    PaperCommandImpl(PaperService service) {
        Assert.notNull(service, "PaperService must be not null!")
        this.service = service
    }

    @Loggable
    @Override
    @HystrixCommand(ignoreExceptions = IllegalStateException,
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    Observable<Optional<Paper>> getByItem(String item) {
        new ObservableResult<Optional<Paper>>() {
            @Override
            Optional<Paper> invoke() {
                LOGGER.debug("Requesting item {}", item)
                ofNullable(service.findByItem(item))
            }
        }
    }

    @Loggable
    @Override
    @HystrixCommand(ignoreExceptions = IllegalStateException,
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    Observable<Optional<Paper>> getByOwner(String item, Optional<OctoUser> user) {
        new ObservableResult<Optional<Paper>>() {
            @Override
            Optional<Paper> invoke() {
                LOGGER.debug("Requesting item {}")
                ofNullable(service.findByItemAndOwner(item, user
                        .orElseThrow({ new UnauthorizedException() })
                        .id.toString()))
            }
        }
    }


    @Loggable
    @Override
    @HystrixCommand
    void delete(String item, Optional<OctoUser> user) {
        OctoUser octoUser = user.orElseThrow({ new UnauthorizedException() })
        this.getByOwner(item, user)
                .map({ it.orElseThrow({ new ResourceNotFoundException() }) })
                .filter({ isOwnedBy(it, octoUser) })
                .subscribe({ service.delete(it) })

    }

    @Override
    Optional<Paper> save(Paper paper, Optional<OctoUser> user) {
        def owner = user.orElseThrow({ new UnauthorizedException() })
        checkMatch(paper, owner)
        ofNullable(service.save(paper))
    }

}
