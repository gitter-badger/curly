package carroll.commons.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Simple implmentation of a CORS filter to add the necessary headers to response to enable this just create a bean
 *
 * @author João Pedro Evangelista
 * @since 15/03/15
 */
public class SimpleCORSFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("Initializing CORS Filter...");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = ((HttpServletRequest) req);

        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
        if (Objects.equals(request.getMethod(), "POST")) {
            response.setHeader("Access-Control-Allow-Origin", "null");
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        //if (!Objects.equals(request.getMethod(), "OPTIONS")) {
            chain.doFilter(req, res);
        //}
    }

    @Override
    public void destroy() {
        logger.debug("Destroying CORS Filter...");
    }
}
