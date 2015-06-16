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
package curly.formula.config

import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

/**
 * @author João Evangelista
 */
@Configuration
@EnableOAuth2Resource
class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {
  override def configure(http: HttpSecurity): Unit = {
    http
      .anonymous()
      .and()
      .antMatcher("/**").authorizeRequests()
      .antMatchers(HttpMethod.GET, "/**").permitAll()
      .anyRequest().permitAll()
  }
}
