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
package curly.artifact;


import curly.commons.config.cache.annotation.EnableRedisCache;
import curly.commons.config.context.EnableRingBufferExecutor;
import curly.commons.logging.annotation.config.EnableLoggable;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * @author João Pedro Evangelista
 * @since 25/04/15
 */
@RibbonClient("artifactory")
@EnableFeignClients
@EnableLoggable
@EnableRingBufferExecutor
@EnableRedisCache
@SpringCloudApplication
public class ArtifactoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtifactoryApplication.class, args);
    }

}
