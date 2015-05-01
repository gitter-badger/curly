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
package curly.artifact;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;

/**
 * @author João Pedro Evangelista
 */
@Service
public class ArtifactServiceImpl implements ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ArtifactServiceImpl(ArtifactRepository artifactRepository, MongoTemplate mongoTemplate) {
        this.artifactRepository = artifactRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {

    }

    @HystrixCommand(fallbackMethod = "defaultFindAll")
    @Override
    public Observable<Optional<Page<Artifact>>> findAll(Pageable pageable) {
        return new ObservableResult<Optional<Page<Artifact>>>() {
            @Override
            public Optional<Page<Artifact>> invoke() {
                return Optional.ofNullable(artifactRepository.findAll(pageable));
            }
        };
    }

    @Override
    @Retryable
    public Observable<Optional<Artifact>> save(Artifact artifact) {
        return new ObservableResult<Optional<Artifact>>() {
            @Override
            public Optional<Artifact> invoke() {
                return Optional.ofNullable(artifactRepository.save(artifact));
            }
        };
    }

    @Override
    @HystrixCommand(fallbackMethod = "defaultFindOne")
    public Observable<Optional<Artifact>> findOne(String id) {
        return new ObservableResult<Optional<Artifact>>() {
            @Override
            public Optional<Artifact> invoke() {
                return Optional.ofNullable(artifactRepository.findOne(id));
            }
        };
    }

    @SuppressWarnings("unused")
    private Optional<Page<Artifact>> defaultFindAll(Pageable pageable) {
        return Optional.of(new PageImpl<>(Collections.emptyList()));
    }

    @SuppressWarnings("unused")
    private Optional<Artifact> defaultFindOne(String id) {
        return Optional.of(new Artifact(id));
    }
}
