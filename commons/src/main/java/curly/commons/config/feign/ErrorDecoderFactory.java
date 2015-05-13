package curly.commons.config.feign;

import curly.commons.config.feign.ex.*;
import org.springframework.http.HttpStatus;

/**
 * @author Joao Pedro Evangelista
 */
public final class ErrorDecoderFactory {

    private ErrorDecoderFactory() {
    }

    public static Exception create(HttpStatus httpStatus, String reason) {
        if (httpStatus.equals(HttpStatus.NOT_FOUND)) {
            return new ResourceNotFoundException(reason);
        } else if (httpStatus.equals(HttpStatus.BAD_REQUEST)) {
            return new BadRequestException(reason);
        } else if (httpStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            return new InternalServerErrorException(reason);
        } else if (httpStatus.equals(HttpStatus.UNAUTHORIZED)) {
            return new UnauthorizedException(reason);
        } else if (httpStatus.equals(HttpStatus.UNSUPPORTED_MEDIA_TYPE)) {
            return new UnsupportedMediaTypeException(reason);
        }
        return new BadRequestException(reason);

    }


}
