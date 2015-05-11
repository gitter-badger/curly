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
package curly.commons.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Dispatcher;
import reactor.core.dispatch.RingBufferDispatcher;
import reactor.core.dispatch.WorkQueueDispatcher;
import reactor.core.dispatch.wait.AgileWaitingStrategy;
import reactor.jarjar.com.lmax.disruptor.dsl.ProducerType;

/**
 * @author Joao Pedro Evangelista
 */
public class DispatcherFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherFactory.class);

    public static Dispatcher workQueueDispatcher(Class<?> loggerClass) {
        return new WorkQueueDispatcher(
                "WorkQueueDispatcher-Factory",
                10,
                1024,
                new ThrowableConsumer(loggerClass),
                ProducerType.MULTI,
                new AgileWaitingStrategy());
    }

    public static Dispatcher ringBufferDispatcher(Class<?> loggerClass) {
        return new RingBufferDispatcher(
                "RingBufferDispatcher-Factory",
                1024,
                new ThrowableConsumer(loggerClass),
                ProducerType.SINGLE,
                new AgileWaitingStrategy()
        );
    }
}
