package carroll

import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

import static org.springframework.http.HttpMethod.GET

@SpringCloudApplication
class EdgeApplication {

    static void main(String[] args) {
        SpringApplication.run EdgeApplication.class, args
    }


    @Configuration
    @EnableOAuth2Resource
    static class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

        @Override
        void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("edge").stateless(true)
        }

        @Override
        void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/heart/**").authenticated()
                    .antMatchers(GET, "/**").permitAll()
        }
    }
}