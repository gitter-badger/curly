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
package curly.edge.artifact.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id
/**
 * @author Joao Pedro Evangelista
 * @since 19/04/2015£
 */
@ToString
@EqualsAndHashCode
class Artifact implements Serializable {

    @Id
    String id

    String author

    String name

    String homePage

    Set<Language> languages

    Set<Tag> tags

    Category category

    String incubation

    String owner

    boolean isNew() {
        id == null
    }
}
