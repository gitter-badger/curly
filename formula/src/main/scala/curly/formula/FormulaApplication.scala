package curly.formula

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.context.annotation.Bean

/**
 * @author João Evangelista
 */
@SpringCloudApplication
class FormulaApplication {
  @Bean def defaultScalaModule() = DefaultScalaModule
}

object Runner {
  def main(args: Array[String]) {
    SpringApplication.run(classOf[FormulaApplication])
  }
}
