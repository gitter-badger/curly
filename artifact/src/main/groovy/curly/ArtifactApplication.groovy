/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package curly

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
