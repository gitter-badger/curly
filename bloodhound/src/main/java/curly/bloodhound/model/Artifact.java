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
package curly.bloodhound.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Set;

/**
 * An artifact representation
 * @author Joao Pedro Evangelista
 */
@Data
@Document(indexName = "artifact")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Artifact implements Serializable {

	private static final long serialVersionUID = 8357768165966900756L;

	private String id;

	private String author;

	private String name;

	private String owner;

	private String homePage;

	private String githubPage;

	private Set<Language> languages;

	private Set<Tag> tags;

	private Category category;

	private String incubation;

	public Artifact() {
	}
}
