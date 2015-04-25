package carroll.commons.config.reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.dispatch.MpscDispatcher;
import reactor.spring.context.config.EnableReactor;
import reactor.spring.webmvc.PromiseHandlerMethodReturnValueHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
@EnableReactor
@Configuration
@EnableConfigurationProperties(ReactorProperties.class)
public class ReactorAutoConfiguration {

    @Autowired
    private ReactorProperties properties;

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Bean
    public PromiseHandlerMethodReturnValueHandler promiseHandlerMethodReturnValueHandler() {
        return new PromiseHandlerMethodReturnValueHandler();
    }

    @PostConstruct
    public void initialize() {
        final List<HandlerMethodReturnValueHandler> originalHandlers = new ArrayList<>(requestMappingHandlerAdapter.getReturnValueHandlers());
        originalHandlers.add(0, promiseHandlerMethodReturnValueHandler());
        requestMappingHandlerAdapter.setReturnValueHandlers(originalHandlers);
    }

    @Bean
    Environment reactorEnv() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    EventBus eventBus(Environment reactorEnv) {
        return EventBus.config()
                .env(reactorEnv)
                .dispatcher(new MpscDispatcher(properties.getDispatcher()))
                .get();
    }


}
