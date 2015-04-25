package carroll.commons.config.web;

/**
 * @author Joao Pedro Evangelista
 * @since 02/04/2015
 */
public class RequiredInterceptorNotFoundException extends RuntimeException {
    public RequiredInterceptorNotFoundException(Class<?> clazz) {
        super("Interceptor " + clazz.getSimpleName() + " Not found");
    }


}
