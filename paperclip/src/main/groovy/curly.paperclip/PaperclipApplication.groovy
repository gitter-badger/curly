package curly.paperclip

import curly.commons.config.cache.annotation.EnableRedisCache
import curly.commons.config.context.EnableWorkQueueExecutor
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication

/**
 * @author Joao Pedro Evangelista
 */
@EnableRedisCache
@EnableWorkQueueExecutor
@SpringCloudApplication
class PaperclipApplication {

    static void main(String[] args) {
        SpringApplication.run PaperclipApplication, args
    }
}
