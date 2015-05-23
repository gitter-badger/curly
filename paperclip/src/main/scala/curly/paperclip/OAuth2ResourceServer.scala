package curly.paperclip

import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity

/**
 * @author Joao Pedro Evangelista
 */
trait OAuth2ResourceServer {

  def configure(http: HttpSecurity): Unit = {
    http
      .anonymous()
      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.GET, "/paperclip/**").permitAll()
      .anyRequest().authenticated()
  }
}
