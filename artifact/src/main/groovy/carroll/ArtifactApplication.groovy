package carroll

import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

/**
 * @author João Pedro Evangelista
 * @since 25/04/15
 */
@SpringCloudApplication
class ArtifactApplication {

    static void main(String[] args) {
        SpringApplication.run ArtifactApplication.class, args
    }

    @EnableOAuth2Resource
    static class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {
        @Override
        void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/libraries").permitAll()
                    .anyRequest().authenticated()
        }
    }

}
