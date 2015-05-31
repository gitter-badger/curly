package curly.paperclip

import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

/**
 * @author Joao Pedro Evangelista
 */
@Configuration
@EnableOAuth2Resource
class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

    @Override
    void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers("/**").authenticated()
    }
}
