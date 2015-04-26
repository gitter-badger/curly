package curly.proxy
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
/**
 * @author João Pedro Evangelista
 */
@EnableZuulProxy
@SpringCloudApplication
class ZuulProxyApplication {

    static void main(String[] args) {
        SpringApplication.run ZuulProxyApplication.class, args
    }

}
