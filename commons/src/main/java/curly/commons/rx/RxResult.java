package curly.commons.rx;

import curly.commons.logging.Loggable;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Scheduler;

import javax.annotation.concurrent.Immutable;

/**
 * Wrapper observable into a DeferredResult for Async processing
 * with Spring since it's own ReturnValueHandler seems affected
 *
 * @author João Pedro Evangelista
 */
@Immutable
public final class RxResult {

    private RxResult(){}

    @Loggable
    public static <T> DeferredResult<T> defer(Observable<T> observable) {
        final DeferredResult<T> deferredResult = new DeferredResult<>();
        observable.subscribe(deferredResult::setResult, deferredResult::setErrorResult);
        return deferredResult;
    }

    @Loggable
    public static <T> DeferredResult<T> defer(Observable<T> observable, Scheduler subScheduler) {
        if (subScheduler == null) {
            return defer(observable);
        } else {
            return defer(observable.subscribeOn(subScheduler));
        }
    }
}
