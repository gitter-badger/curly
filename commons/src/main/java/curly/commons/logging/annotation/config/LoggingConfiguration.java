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
package curly.commons.logging.annotation.config;

import curly.commons.logging.MethodExecutionAspectInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;

/**
 * @author Joao Pedro Evangelista
 */
@Configuration
@ComponentScan("curly.commons.logging")
@EnableMBeanExport
@EnableAspectJAutoProxy
public class LoggingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    MethodExecutionAspectInterceptor methodExecutionAspectInterceptor() {
        return new MethodExecutionAspectInterceptor();
    }
}