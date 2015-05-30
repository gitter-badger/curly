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
package curly.merchant

import com.gemstone.gemfire.cache.GemFireCache
import curly.merchant.domain.ExportedOctoRepository
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.CacheFactoryBean
import org.springframework.data.gemfire.LocalRegionFactoryBean
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

/**
 * @author Joao Pedro Evangelista
 */
@SpringCloudApplication
class MerchantApplication {

    static void main(String[] args) {
        SpringApplication.run MerchantApplication, args
    }

    @Configuration
    @EnableGemfireRepositories
    static class GemfireConfiguration {

        @Bean CacheFactoryBean cacheFactoryBean() {
            new CacheFactoryBean()
        }

        @Bean
        LocalRegionFactoryBean<String, ExportedOctoRepository> exportedOctoRepositoryLocalRegion(GemFireCache gemfireCache) {
            def factoryBean = new LocalRegionFactoryBean<String, ExportedOctoRepository>()
            factoryBean.setCache(gemfireCache)
            factoryBean.setName("exportedOctoRepository")
            factoryBean.setClose(false)
            factoryBean
        }

    }
}
