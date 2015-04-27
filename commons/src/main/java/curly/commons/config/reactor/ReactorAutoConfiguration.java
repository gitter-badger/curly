/*
 * Copyright 2015 the original author or authors.
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
package curly.commons.config.reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.dispatch.MpscDispatcher;
import reactor.spring.context.config.EnableReactor;

/**
 * @author Joao Pedro Evangelista
 */
@EnableReactor
@Configuration
@EnableConfigurationProperties(ReactorProperties.class)
public class ReactorAutoConfiguration {

    @Autowired
    private ReactorProperties properties;

    @Bean
    @Reactor
    Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    @Reactor
    EventBus eventBus(Environment reactorEnv) {
        return EventBus.config()
                .env(reactorEnv)
                .dispatcher(new MpscDispatcher("MpscDispatcher"))
                .get();
    }


}
