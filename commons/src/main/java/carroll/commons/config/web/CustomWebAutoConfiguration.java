package carroll.commons.config.web;

import carroll.commons.github.GitHubAuthenticationMethodHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import rx.Observable;

import java.util.List;

/**
 * Configuration for global customization of WebMvcConfigurerAdapter
 * features
 *
 * @author João Pedro Evangelista
 * @since 26/03/2015
 */
@Configuration
public class CustomWebAutoConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    @ConditionalOnClass(Observable.class)
    @ConditionalOnMissingBean
    public ObservableMethodReturnValueHandler observableMethodReturnValueHandler() {
        return new ObservableMethodReturnValueHandler();
    }

    @Bean
    public GitHubAuthenticationMethodHandler githubAuthenticationMethodHandler(){
        return new GitHubAuthenticationMethodHandler();
    }


    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
        returnValueHandlers.add(observableMethodReturnValueHandler());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(githubAuthenticationMethodHandler());
    }
}
