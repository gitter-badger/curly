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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class to bootstrap cache for Redis using the properties {@code io.redis.cache.cacheNames=foo,bar,baz
 * io.redis.cache.cacheExpirations=1000,2000,3000 }
 * <p>
 * <b>Note:</b> for each index on properties list you need to provide in the same index the desired expiration
 * <p>
 * As name: foo, expiration = 1000 name: bar, expiration = 2000
 *
 * @author Joao Pedro Evangelista
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisCacheProperties.class)
@ConditionalOnClass({Jedis.class, RedisTemplate.class})
public class RedisCacheConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisCacheProperties redisCacheProperties;

    @Bean
    RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    @DependsOn("redisTemplate")
    CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setCacheNames(redisCacheProperties.getCacheNames());
        redisCacheManager.setExpires(toMap(redisCacheProperties.getCacheNames(), redisCacheProperties.getCacheExpirations()));
        return redisCacheManager;
    }


    private Map<String, Long> toMap(final List<String> cacheNames, final List<Long> cacheExpirations) {
        Map<String, Long> map = new HashMap<>();
        logger.debug("Converting cache resources to map, {}, {}", cacheNames.toString(), cacheExpirations.toString());
        for (String cacheName : cacheNames) {
            logger.debug("Converting {} from list to map", cacheName);
            for (Long cacheExpiration : cacheExpirations) {
                logger.debug("Putting on map, key {}, value {}", cacheName, cacheExpiration);
                map.put(cacheName, cacheExpiration);
            }
        }
        return map;
    }
}

