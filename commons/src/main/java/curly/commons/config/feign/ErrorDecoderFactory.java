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
