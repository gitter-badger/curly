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

import curly.commons.security.SimpleUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.security.Principal;

/**
 * Github user representation
 *
 * @author Joao Pedro Evangelista
 * @since 06/04/2015
 */

@Getter
@EqualsAndHashCode(callSuper = true)
public class OctoUser extends SimpleUser<Long> implements Serializable, Principal {

    public static final String TYPE_USER = "User";

    public static final String TYPE_ORG = "Organization";

    private static final long serialVersionUID = -1211802439119529774L;

    private String email;

    private boolean hireable;

    private int followers;

    private int following;

    private int publicRepos;

    private String avatarUrl;

    private String blog;

    private String company;

    private String gravatarId;

    private String htmlUrl;

    private String location;

    private String login;

    private String name;

    private String type;

    private String url;

    public OctoUser(String email, boolean hireable, long id, int followers, int following, int publicRepos, String avatarUrl,
                    String blog, String company, String gravatarId, String htmlUrl, String location, String login,
                    String name, String type, String url) {
        super(id);
        this.email = email;
        this.hireable = hireable;
        this.followers = followers;
        this.following = following;
        this.publicRepos = publicRepos;
        this.avatarUrl = avatarUrl;
        this.blog = blog;
        this.company = company;
        this.gravatarId = gravatarId;
        this.htmlUrl = htmlUrl;
        this.location = location;
        this.login = login;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public OctoUser(Long id) {
        super(id);
    }

    public OctoUser() {
    }
}
