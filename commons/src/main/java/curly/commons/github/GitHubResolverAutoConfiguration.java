package curly.commons.github;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author João Pedro Evangelista
 */
@Configuration
public class GitHubResolverAutoConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    GitHubAuthenticationMethodHandler gitHubAuthenticationMethodHandler(){
        return new GitHubAuthenticationMethodHandler();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(gitHubAuthenticationMethodHandler());
    }
}
