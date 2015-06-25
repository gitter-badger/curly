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
package curly.artifact.service;

import curly.artifact.model.Artifact;
import curly.commons.github.OctoUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rx.Observable;

import java.util.Optional;

/**
 * @author João Evangelista
 */
public interface ArtifactCommand {

	Observable<Optional<Page<Artifact>>> findAllByPage(Pageable pageable);


	Observable<Optional<Artifact>> save(Artifact artifact, OctoUser octoUser);

	Observable<Optional<Artifact>> findOne(String id);

	void delete(String id, OctoUser octoUser);
}
