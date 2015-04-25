package carroll.commons.config.web;

import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

/**
 * @author João Pedro Evangelista
 * @since 26/03/2015
 */
public class ObservableAdapter<T> extends DeferredResult<T> {

    public ObservableAdapter(Observable<T> observable) {
        observable.subscribe(this::setResult, this::setErrorResult);
    }
}
