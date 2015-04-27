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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import rx.Observable

/**
 * @author João Pedro Evangelista
 */
interface ArtifactCommand {
    Observable<Optional<Page<Artifact>>> artifacts(Pageable pageable)

    Observable<Optional<Artifact>> artifact(String id)

    Observable<Optional<Artifact>> save(Artifact artifact)

    Observable<Optional<Page<Artifact>>> byLanguage(String language, Pageable pageable)

    Observable<Optional<Page<Artifact>>> byType(String type, Pageable pageable)

    Observable<Optional<Page<Artifact>>> byCategory(String category, Pageable pageable)

    void delete(Artifact artifact)

}
