package curly.translator

import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

/**
 * @author João Evangelista
 */
Configuration
EnableOAuth2Resource
public open class OAuth2ResourceConfig : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http!!.anonymous().and().antMatcher("/**").authorizeRequests().antMatchers("/**").permitAll()
    }
}
