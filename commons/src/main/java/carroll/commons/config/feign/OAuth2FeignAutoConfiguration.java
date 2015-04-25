package carroll.commons.config.feign;

import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.security.oauth2.client.OAuth2LoadBalancerClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.util.Assert;

/**
 * @author Joao Pedro Evangelista
 * @since 06/04/2015
 */
@Configuration
@ConditionalOnClass({Feign.class,OAuth2ClientContext.class})
@AutoConfigureAfter({OAuth2LoadBalancerClientAutoConfiguration.class})
public class OAuth2FeignAutoConfiguration {

    @Bean
    @ConditionalOnBean(OAuth2ClientContext.class)
    @ConditionalOnMissingBean
    @ConditionalOnClass(RequestInterceptor.class)
    RequestInterceptor requestInterceptor(OAuth2ClientContext context){
        Assert.notNull(context,"OAuth2ClientContext must be not null!");
        return new OAuth2FeignRequestInterceptor(context);
    }
}
