package curly.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

/**
 * @author João Pedro Evangelista
 */
@EnableEurekaServer
@SpringBootApplication
class EurekaServerApplication {

    static void main(String[] args) {
        SpringApplication.run EurekaServerApplication.class, args
    }
}
