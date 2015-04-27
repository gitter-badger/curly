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
package curly.artifact

import com.google.common.base.Optional
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.retry.annotation.Retryable
import rx.Observable

import javax.validation.constraints.NotNull

/**
 * Default implementation for Hystrix commands to wrap
 * executions on database which might fail providing resilience to API
 *
 * @author João Pedro Evangelista
 */
class PrimaryArtifactCommand implements ArtifactCommand {

    private @Autowired
    @NotNull
    ArtifactRepository repository

    /**
     *
     * @param pageable incoming from controller
     * @return a page of artifacts
     */
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultArtifacts")
    Observable<Optional<Page<Artifact>>> artifacts(Pageable pageable) {
        return new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.fromNullable(repository.findAll(pageable))
            }
        }
    }

    /**
     * Get an artifact based on id, null if not found or exception triggering the fallback
     * @param id of an artifact
     * @return Artifact Observable Result
     */
    @Override
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultArtifact")
    Observable<Optional<Artifact>> artifact(String id) {
        new ObservableResult<Optional<Artifact>>() {
            @Override
            Optional<Artifact> invoke() {
                Optional.fromNullable repository.findOne(id)
            }
        }
    }

    @Override
    @Retryable
    void delete(Artifact artifact) {
        repository.delete(artifact)
    }

    @Override
    @Retryable
    Observable<Optional<Artifact>> save(Artifact artifact) {
        if (artifact.id == null) artifact.id = ObjectId.get().toHexString()
        Observable.just(Optional.fromNullable(repository.save(artifact)))
    }

    @Override
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultByLanguage")
    Observable<Optional<Page<Artifact>>> byLanguage(String language, Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.fromNullable(repository.findByLanguages(language, pageable))
            }
        }
    }

    @Override
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultByType")
    Observable<Optional<Page<Artifact>>> byType(String type, Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.fromNullable(repository.findByTypes(type, pageable))
            }
        }
    }

    @Override
    @Retryable
    @HystrixCommand(fallbackMethod = "defaultByCategory")
    Observable<Optional<Page<Artifact>>> byCategory(String category, Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.fromNullable(repository.findByCategory(category, pageable))
            }
        }
    }

    /*
    * -----------------------------------------------------------------
    * ------------------------- Fallbacks -----------------------------
    * -----------------------------------------------------------------
    */

    /**
     * @see #artifact(java.lang.String)
     */
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
    private Observable<Optional<Artifact>> defaultArtifact(String id) {
        new ObservableResult<Optional<Artifact>>() {
            @Override
            Optional<Artifact> invoke() {
                Optional.of(new Artifact(id: id))
            }
        }
    }

    /**
     * @see #artifacts(org.springframework.data.domain.Pageable)
     */
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
    private Observable<Optional<Page<Artifact>>> defaultArtifacts(Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.of(new PageImpl<Artifact>(Collections.emptyList()))
            }
        }
    }

    /**
     * @see #byLanguage(java.lang.String, org.springframework.data.domain.Pageable)
     */
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
    private Observable<Optional<Page<Artifact>>> defaultByLanguage(String language, Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.of(new PageImpl<Artifact>(Collections.emptyList()))
            }
        }
    }

    /**
     * @see #byCategory(java.lang.String, org.springframework.data.domain.Pageable)
     */
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
    private Observable<Optional<Page<Artifact>>> defaultByCategory(String category, Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.of(new PageImpl<Artifact>(Collections.emptyList()))
            }
        }
    }
    /**
     * @see #byType(java.lang.String, org.springframework.data.domain.Pageable)
     */
    @SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
    private Observable<Optional<Page<Artifact>>> defaultByType(String type, Pageable pageable) {
        new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            Optional<Page<Artifact>> invoke() {
                Optional.of(new PageImpl<Artifact>(Collections.emptyList()))
            }
        }
    }
}

