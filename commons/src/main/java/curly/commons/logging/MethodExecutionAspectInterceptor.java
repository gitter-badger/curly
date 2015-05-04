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

import curly.commons.logging.annotation.Loggable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.StopWatch;

import static curly.commons.logging.LogLevelResolver.log;

/**
 * @author Joao Pedro Evangelista
 */
@Aspect
@ManagedResource(objectName = "curly.logging:type=Logging",
        description = "Logging timer and trace functionality for system")
public class MethodExecutionAspectInterceptor {

    private boolean executionEnabled = true;

    private boolean throwableEnable = true;

    private boolean actuatorEnabled = true;


    @Autowired(required = false)
    private GaugeService gaugeService;

    @Autowired(required = true)
    private CounterService counterService;

    @Around(value = "execution(* *(..)) && @annotation(loggable)) ", argNames = "joinPoint, loggable")
    public Object invoke(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        if (executionEnabled) {
            StopWatch watch = new StopWatch(joinPoint.toShortString());
            watch.start();
            try {
                return joinPoint.proceed();
            } finally {
                watch.stop();
                synchronized (this) {
                    if (actuatorEnabled && (gaugeService != null)) {
                        String gagueName = "gauge.execution." + joinPoint.getTarget() + "." + joinPoint.getSignature().getName();
                        gaugeService.submit(gagueName, watch.getLastTaskTimeMillis());
                    }
                    log(loggable.value(), joinPoint.getTarget().getClass(),
                            "Executed method {} in {} ms",
                            joinPoint.getSignature().getName(),
                            watch.getLastTaskTimeMillis());
                }
            }

        }
        return joinPoint.proceed();
    }

    @AfterThrowing(
            pointcut = "execution(* * (..)) && @annotation(curly.commons.logging.annotation.Loggable)",
            throwing = "throwable", argNames = "joinPoint,throwable")
    public void invokeAfterThrow(JoinPoint joinPoint, Throwable throwable) {
        if (throwableEnable) {
            if (actuatorEnabled && (counterService != null)) {
                String counterName = "counter.threw." +
                        joinPoint.getTarget() +
                        "." + joinPoint.getSignature().getName() +
                        "#" + throwable.getClass().getSimpleName();
                counterService.increment(counterName);
            }
            log(LogLevel.ERROR,
                    joinPoint.getTarget().getClass(),
                    "Error processing method {}, nested exception is {}",
                    joinPoint.getSignature().getName(), throwable);
        }
    }

    @ManagedAttribute
    public boolean isExecutionEnabled() {
        return executionEnabled;
    }

    @ManagedAttribute
    public void setExecutionEnabled(boolean executionEnabled) {
        this.executionEnabled = executionEnabled;
    }

    @ManagedAttribute
    public boolean isThrowableEnable() {
        return throwableEnable;
    }

    @ManagedAttribute
    public void setThrowableEnable(boolean throwableEnable) {
        this.throwableEnable = throwableEnable;
    }

    @ManagedAttribute
    public boolean isActuatorEnabled() {
        return actuatorEnabled;
    }

    @ManagedAttribute
    public void setActuatorEnabled(boolean actuatorEnabled) {
        this.actuatorEnabled = actuatorEnabled;
    }
}
