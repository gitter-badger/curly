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
package curly.commons.security.negotiation;

/**
 * @author Joao Pedro Evangelista
 */
public class OperationViolationException extends RuntimeException {

    private static final long serialVersionUID = -1729290014353488481L;

    public OperationViolationException() {
        super("Error occurred during assertions on process of entity");
    }

    public OperationViolationException(String message) {
        super("Error occurred during assertions on process of entity. " + message);
    }

    public OperationViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
