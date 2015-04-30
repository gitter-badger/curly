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
package curly.commons.rx;

import com.jcabi.aspects.Loggable;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;

import javax.annotation.concurrent.Immutable;

/**
 * Wrapper observable into a DeferredResult for Async processing
 * with Spring since it's own ReturnValueHandler seems affected
 *
 * @author João Pedro Evangelista
 */
@Immutable
public final class RxResult {

    private RxResult() {
    }

    @Loggable
    public static <T> DeferredResult<T> defer(Observable<T> observable, Scheduler subScheduler) {
        if (subScheduler == null) {
            return defer(observable);
        } else {
            return defer(observable.subscribeOn(subScheduler));
        }
    }

    @Loggable
    public static <T> DeferredResult<T> defer(Observable<T> observable) {
        DeferredResult<T> deferredResult = new DeferredResult<>();
        observable.subscribe(new Action1<T>() {
            @Override public void call(T t) {
                deferredResult.setResult(t);
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                deferredResult.setErrorResult(throwable);
            }
        });
        return deferredResult;
    }
}
