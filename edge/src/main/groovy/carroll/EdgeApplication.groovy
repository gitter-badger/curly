package carroll
import curly.commons.config.web.ObservableMethodReturnValueHandler
import curly.commons.github.GitHubAuthenticationMethodHandler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import reactor.spring.webmvc.PromiseHandlerMethodReturnValueHandler

import static org.springframework.http.HttpMethod.GET

@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
class EdgeApplication extends WebMvcConfigurerAdapter {

    static void main(String[] args) {
        SpringApplication.run EdgeApplication.class, args
    }

    @Override
    void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(0,promiseHandlerMethodReturnValueHandler())
        returnValueHandlers.add(1,observableMethodReturnValueHandler())
    }

    @Bean
    public ObservableMethodReturnValueHandler observableMethodReturnValueHandler() {
        return new ObservableMethodReturnValueHandler();
    }

    @Bean
    public PromiseHandlerMethodReturnValueHandler promiseHandlerMethodReturnValueHandler() {
        return new PromiseHandlerMethodReturnValueHandler();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(githubAuthenticationMethodHandler());
    }

    @Bean
    public GitHubAuthenticationMethodHandler githubAuthenticationMethodHandler() {
        return new GitHubAuthenticationMethodHandler();
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
