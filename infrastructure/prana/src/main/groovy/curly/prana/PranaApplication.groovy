package curly.prana

import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.sidecar.EnableSidecar

/**
 * @author João Pedro Evangelista
 */
@EnableSidecar
@SpringCloudApplication
public class PranaApplication {

    static void main(String[] args) {
        SpringApplication.run PranaApplication.class, args
    }

}
