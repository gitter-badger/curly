package carroll.commons.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author João Pedro Evangelista
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ResourceUnauthorized extends RuntimeException {

    public ResourceUnauthorized() {
        super("Unauthorized request!");
    }

    public ResourceUnauthorized(String message) {
        super(message);
    }

    public ResourceUnauthorized(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceUnauthorized(Throwable cause) {
        super(cause);
    }

    protected ResourceUnauthorized(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
