package carroll.commons.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;

/**
 * @author Joao Pedro Evangelista
 * @since 06/04/2015
 */
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);

    private final OAuth2ClientContext oAuth2ClientContext;

    private final String tokenType;

    private final String header;

    /**
     * Fully customizable constructor for changing token type and header name, in cases of Bearer and Authorization is not the default
     * such as "bearer", "authorization"
     *
     * @param oAuth2ClientContext
     * @param tokenType
     * @param header
     */
    public OAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext, String tokenType, String header) {
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.tokenType = tokenType;
        this.header = header;
    }

    /**
     * Default constructor which uses the provided OAuth2ClientContext and
     * Bearer tokens within Authorization header
     *
     * @param oAuth2ClientContext provided context
     */
    public OAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext) {
        this(oAuth2ClientContext, "Bearer", "Authorization");
        logger.debug("Constructing default OAuth2FeignRequestInterceptor");
    }
    /**
     *
     * @see RequestInterceptor#apply(RequestTemplate)
     */
    @Override
    public void apply(RequestTemplate template) {
        logger.debug("Applying RequestInterceptor customization");
        template.header(header, value(tokenType, oAuth2ClientContext));
    }

    private String value(String tokenType, OAuth2ClientContext context) {
        final AccessTokenRequest accessTokenRequest = context.getAccessTokenRequest();
        if (accessTokenRequest != null) {
            logger.debug("Returning token {} value", tokenType);
            return String.format("%s %s", tokenType, accessTokenRequest.getExistingToken().toString());
        }
        logger.debug("No accessTokenRequest found for Feign RequestTemplate!");
        return "";
    }


}
