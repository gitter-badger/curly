/*
 * Copyright 2015 the original author or authors.
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
package curly.commons.web.hateoas;

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

    public static final String PROTOBUF = "application/x-protobuf";
}
