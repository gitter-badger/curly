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
package curly.paperclip.paper

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.commons.config.feign.ex.UnauthorizedException
import curly.commons.github.OctoUser
import curly.commons.logging.annotation.Loggable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import rx.Observable

import javax.inject.Inject

import static curly.paperclip.paper.OwningAssert.matchOwners
import static curly.paperclip.paper.OwningAssert.wasModified
import static java.util.Optional.empty
import static java.util.Optional.ofNullable


/**
 * @author Joao Pedro Evangelista
 */
@Service
class PaperCommandImpl implements PaperCommand {

    private PaperRepository repository

    private static final Logger logger = LoggerFactory.getLogger(PaperCommandImpl)

    @Inject
    PaperCommandImpl(PaperRepository paperRepository) {
        this.repository = paperRepository
    }

    @Loggable
    @Override
    @Retryable
    @HystrixCommand
    Observable<Optional<Paper>> getByItem(String item) {
        new ObservableResult<Optional<Paper>>() {
            @Override
            Optional<Paper> invoke() {
                ofNullable(repository.findByItem(item))
            }
        }
    }

    @Loggable
    @Override
    @Retryable
    @HystrixCommand
    Observable<Optional<Paper>> getByOwner(String item, Optional<OctoUser> user) {
        new ObservableResult<Optional<Paper>>() {
            @Override
            Optional<Paper> invoke() {
                ofNullable(repository.findByItemAndOwner(item, user
                        .orElseThrow({ new UnauthorizedException() })
                        .id.toString()))
            }
        }
    }


    @Loggable
    @Override
    @Retryable
    @HystrixCommand
    void delete(Paper paper, Optional<OctoUser> user) {
        def owner = user.orElseThrow({ new UnauthorizedException() }).id.toString()
        if (paper.getOwner() == owner) {
            logger.info("Paper's and owner matched, deleting {}", paper.id)
            repository.delete(paper)
        } else {
            logger.warn(
                    "Attempt of deleting a non owned resource cotgh, who attempted {} in resource {} of {} ",
                    owner, paper.id, paper.owner)
        }
    }

    @Override
    Optional<Paper> save(Paper paper, Optional<OctoUser> user) {
        def owner = user.orElseThrow({ new UnauthorizedException() }).id.toString()
        if (merge(paper)) {
            if (wasModified(repository.findOne(paper.id), owner, paper)) return ofNullable(repository.save(paper))
        } else if (matchOwners(paper, owner)) return ofNullable(repository.save(paper))
        logger.error("Cannot assign a paper for the one which was modified or it's owners don't match")
        empty()
    }

    private static boolean merge(Paper paper) {
        paper.id != null
    }
}
