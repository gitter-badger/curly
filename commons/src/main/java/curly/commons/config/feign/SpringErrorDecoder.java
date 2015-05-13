package curly.commons.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author Joao Pedro Evangelista
 */
@Slf4j
public class SpringErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (log.isErrorEnabled()) {
            log.error("Feign threw a exception from method {} with response {}", methodKey, response);
        }
        return ErrorDecoderFactory.create(parseResponse(response), response.reason());
    }


    private HttpStatus parseResponse(Response response) {
        return HttpStatus.valueOf(response.status());
    }
}
