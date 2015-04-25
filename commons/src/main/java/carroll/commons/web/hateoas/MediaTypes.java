package carroll.commons.web.hateoas;

import org.springframework.data.rest.webmvc.RestMediaTypes;

/**
 * @author Joao Pedro Evangelista
 */
public class MediaTypes extends RestMediaTypes {

    public static final String HAL_JSON = "application/hal+json";

    public static final String JSON_PATCH_JSON = "application/json-patch+json";

    public static final String MERGE_PATCH_JSON = "application/merge-patch+json";

    public static final String SCHEMA_JSON = "application/schema+json";

    public static final String SPRING_DATA_VERBOSE_JSON = "application/x-spring-data-verbose+json";

    public static final String SPRING_DATA_COMPACT_JSON = "application/x-spring-data-compact+json";

}
