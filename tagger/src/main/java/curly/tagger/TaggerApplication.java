package curly.tagger;

import curly.commons.config.context.EnableWorkQueueExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author João Evangelista
 */
@EnableRetry
@EnableWorkQueueExecutor
@EnableOAuth2Resource
@SpringCloudApplication
public class TaggerApplication extends ResourceServerConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(TaggerApplication.class);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**").authorizeRequests().antMatchers(HttpMethod.GET, "/**").permitAll();
	}

}

/**
 * todo: feign communication with Artifactory as fallback for amqp send
 * todo: documentation
 */
