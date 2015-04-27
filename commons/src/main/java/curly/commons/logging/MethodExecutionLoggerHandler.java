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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * AspectJ class to cut the method execution with {@link Loggable}
 *
 * @author João Pedro Evangelista
 * @since 0.0.1
 */
@Aspect
public class MethodExecutionLoggerHandler {

    private final StopWatch stopWatch = new StopWatch("MethodExecutionLoggerHandler");

    @Around("execution(* *(..)) && @annotation(Loggable)")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {

        final Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();

        stopWatch.start(method.getName());
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            synchronized (this) {
                StopWatch.TaskInfo info = stopWatch.getLastTaskInfo();
                LoggerFactory.getLogger(method.getDeclaringClass())
                        .info("Execution of {} took {}ms", method.getName(), TimeUnit.MILLISECONDS.convert(info.getTimeMillis(), TimeUnit.MILLISECONDS));
            }
        }
    }
}
