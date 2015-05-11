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
import reactor.fn.Consumer;


/**
 * @author Joao Pedro Evangelista
 */
public class ThrowableConsumer implements Consumer<Throwable> {

    private final Logger logger;

    public ThrowableConsumer(Class<?> aClass) {
        this.logger = LoggerFactory.getLogger(aClass);
    }

    @Override
    public void accept(Throwable throwable) {
        logger.error("Cannot Process Task", throwable);
    }
}
