/*
 * Copyright 2015 the original author or authors.
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
package curly.edge.artifact
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

import java.time.LocalDate
/**
 * @author Joao Pedro Evangelista
 * @since 19/04/2015
 */
class Artifact implements  Serializable {

    @Id
    String id

    String author

    String name

    String homePage

    Set<Language> languages

    Set<Type> types

    Category category

    LocalDate incubation

    String owner

    Artifact() {
        this.id = id == null ? ObjectId.get().toHexString() : id;
    }

    boolean isNew(){
        id == null
    }
}
