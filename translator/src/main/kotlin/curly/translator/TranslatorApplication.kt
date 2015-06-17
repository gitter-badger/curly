package curly.translator

import curly.commons.config.context.EnableRingBufferExecutor
import curly.commons.logging.annotation.config.EnableLoggable
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import kotlin.platform.platformStatic

/**
 * @author João Evangelista
 */
EnableLoggable
EnableRingBufferExecutor
SpringCloudApplication
open class TranslatorApplication {

    companion object {
        platformStatic public fun main(args: Array<String>) {
            SpringApplication.run(javaClass<TranslatorApplication>(), *args)
        }
    }

}
