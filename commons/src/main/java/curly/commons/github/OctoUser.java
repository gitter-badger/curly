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
package curly.commons.github;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import java.io.Serializable;

/**
 * Github user representation
 *
 * @author Joao Pedro Evangelista
 * @since 06/04/2015
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OctoUser implements Serializable {

    public static final String TYPE_USER = "User";

    public static final String TYPE_ORG = "Organization";

    private static final long serialVersionUID = -1211802439119529774L;

    private boolean hireable;

    private int followers;

    private int following;

    private int id;

    private int publicRepos;

    private String avatarUrl;

    private String blog;

    private String company;

    private String email;

    private String gravatarId;

    private String htmlUrl;

    private String location;

    private String login;

    private String name;

    private String type;

    private String url;

}
