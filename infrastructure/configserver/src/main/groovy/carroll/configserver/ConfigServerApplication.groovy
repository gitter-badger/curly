package carroll.configserver

import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.config.server.EnableConfigServer

/**
 * @author João Pedro Evangelista
 */
@EnableConfigServer
@SpringCloudApplication
public class ConfigServerApplication {
    static void main(String[] args) {
        SpringApplication.run ConfigServerApplication.class, args
    }
}
