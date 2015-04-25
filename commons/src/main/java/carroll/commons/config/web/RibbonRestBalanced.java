package carroll.commons.config.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.security.oauth2.client.OAuth2LoadBalancerClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Joao Pedro Evangelista
 * @since 02/04/2015
 */
//@EnableFeignClients("librarian")
@Configuration
@AutoConfigureAfter({OAuth2LoadBalancerClientAutoConfiguration.class})
public class RibbonRestBalanced {

    private static final Logger logger = LoggerFactory.getLogger(RibbonRestBalanced.class);

    private static final String TOKEN_TYPE= "Bearer";

    private static final String HEADER = "Authorization";

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;


    @PostConstruct
    public void initialize() {
        logger.trace("Initializing RibbonRestBalanced");
        oAuth2RestTemplate.setMessageConverters(httpMessageConverters());

    }

    private List<HttpMessageConverter<?>> httpMessageConverters() {
        List<HttpMessageConverter<?>> httpMessageConverters = oAuth2RestTemplate.getMessageConverters();
        httpMessageConverters.addAll(this.halHttpMessageConverters());
        return httpMessageConverters;
    }

    private List<HttpMessageConverter<?>> halHttpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jackson2HalModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


        converter.setObjectMapper(mapper);

        converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON));
        converters.add(converter);

        return converters;
    }


}

