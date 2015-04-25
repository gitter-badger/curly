package carroll.commons.github;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
@EqualsAndHashCode
public class OctoUser implements Serializable {

    private static final long serialVersionUID = -1211802439119529774L;

    public static final String TYPE_USER = "User";

    public static final String TYPE_ORG = "Organization";

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
