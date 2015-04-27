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
package curly.commons.config.context;

import curly.commons.config.reactor.Reactor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.Environment;
import reactor.spring.context.config.EnableReactor;
import reactor.spring.core.task.RingBufferAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author João Pedro Evangelista
 * @since 27/03/2015
 */
@EnableAsync
@Configuration
@ConditionalOnClass({EnableReactor.class, RingBufferAsyncTaskExecutor.class})
public class RingBufferExecutorAutoConfiguration implements AsyncConfigurer {


    @Autowired
    @Reactor
    private Environment env;

    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor();
    }

    /**
     * Creates a Reactor's RingBuffer AsyncTaskExecutor to be used
     * with Spring Async processing
     *
     * @return configured RingBufferAsyncTaskExecutor
     */
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        RingBufferAsyncTaskExecutor executor = new RingBufferAsyncTaskExecutor(env);
        executor.setBacklog(2048);
        executor.setName("ringBufferAsyncTaskExecutor");
        executor.setProducerType(reactor.jarjar.com.lmax.disruptor.dsl.ProducerType.SINGLE);
        executor.setWaitStrategy(new reactor.jarjar.com.lmax.disruptor.YieldingWaitStrategy());
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncUncaughtExceptionHandler();
    }

    @Bean
    public AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
