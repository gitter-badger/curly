package carroll.commons.config.cache;

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
