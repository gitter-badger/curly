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
package curly.smuggler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import curly.commons.github.OctoUser
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * A GitHub repository representation for internal usages when acquiring data from API.
 *
 * This class can be converted into a {@link ExportedOctoRepository} for data manipulation
 * and representation on app.
 *
 * @author Joao Pedro Evangelista
 */
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
class OctoRepository implements Serializable {

    boolean fork;

    boolean hasDownloads;

    boolean hasIssues;

    boolean hasWiki;

    @JsonProperty("private")
    boolean isPrivate;

    Date createdAt;

    Date pushedAt;

    Date updatedAt;

    int forks;

    long id;

    int openIssues;

    int size;

    int watchers;

    OctoRepository parent;

    OctoRepository source;

    String cloneUrl;

    String description;

    String homepage;

    String gitUrl;

    String htmlUrl;

    String language;

    String masterBranch;

    String mirrorUrl;

    String name;

    String sshUrl;

    String svnUrl;

    String url;

    OctoUser owner;
}
