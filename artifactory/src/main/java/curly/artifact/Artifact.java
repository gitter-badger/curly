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

import curly.commons.security.OwnedResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joao Pedro Evangelista
 * @since 19/04/2015
 */
@Data
@Document
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Artifact extends OwnedResource implements Identifiable<String>, Serializable {

    private static final long serialVersionUID = 8357768165966900756L;

    @Id
    private String id;

    private String author;

    private String name;

    private String homePage;

    private Set<Language> languages;

    private Set<Type> types;

    private Category category;

    private String incubation;

    public Artifact() {
        this.id = (id == null) ? ObjectId.get().toHexString() : id;
    }

    public Artifact(String id) {
        this.id = id;
        this.author = "";
        this.name = "";
        this.homePage = "";
        this.languages = new HashSet<>(0);
        this.types = new HashSet<>(0);
        this.category = new Category();
        this.incubation = "";
        this.owner = "";
    }

    @Override
    public String getId() {
        return id;
    }
}
