package carroll.commons.config.context;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
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
public class RingBufferExecutorAutoConfiguration {

    @Bean
    public AsyncConfigurer asyncConfigurer(final Environment env) {
        return new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                return asyncTaskExecutor(env);
            }

            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return new SimpleAsyncUncaughtExceptionHandler();
            }
        };
    }

    /**
     * Creates a Reactor's RingBuffer AsyncTaskExecutor to be used
     * with Spring Async processing
     *
     * @param env the default Environment
     * @return configured RingBufferAsyncTaskExecutor
     */
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor(Environment env) {
        RingBufferAsyncTaskExecutor executor = new RingBufferAsyncTaskExecutor(env);
        executor.setBacklog(2048);
        executor.setName("ringBufferAsyncTaskExecutor");
        executor.setProducerType(reactor.jarjar.com.lmax.disruptor.dsl.ProducerType.SINGLE);
        executor.setWaitStrategy(new reactor.jarjar.com.lmax.disruptor.YieldingWaitStrategy());
        return executor;
    }
}
