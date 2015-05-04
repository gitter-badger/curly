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
package curly.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joao Pedro Evangelista
 */
public class LogLevelResolver {


    public static void log(LogLevel logLevel, Class<?> klass, String format, Object... params) {
        Logger logger = LoggerFactory.getLogger(klass);
        if (logLevel.equals(LogLevel.TRACE)) {
            logger.trace(format, params);
        } else if (logLevel.equals(LogLevel.DEBUG)) {
            logger.debug(format, params);
        } else if (logLevel.equals(LogLevel.INFO)) {
            logger.info(format, params);
        } else if (logLevel.equals(LogLevel.WARN)) {
            logger.warn(format, params);
        } else if (logLevel.equals(LogLevel.ERROR)) {
            logger.error(format, params);
        }
    }
}
