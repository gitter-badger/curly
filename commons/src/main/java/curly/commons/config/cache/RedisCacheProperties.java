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
package curly.commons.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
@Component
@ConfigurationProperties(prefix = "io.redis.cache")
public class RedisCacheProperties {

    private List<String> cacheNames = new ArrayList<>();

    private List<Long> cacheExpirations = new ArrayList<>();

    public List<String> getCacheNames() {
        return cacheNames;
    }

    public void setCacheNames(List<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    public List<Long> getCacheExpirations() {
        return cacheExpirations;
    }

    public void setCacheExpirations(List<Long> cacheExpirations) {
        this.cacheExpirations = cacheExpirations;
    }
}
