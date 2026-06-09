package constellation.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheConfig implements CachingConfigurer {

  private ObjectMapper redisObjectMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false)
        .activateDefaultTyping(
            com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY)
        .build();
  }

  private RedisCacheConfiguration buildCacheConfig(Duration ttl) {
    GenericJackson2JsonRedisSerializer jsonSerializer =
        new GenericJackson2JsonRedisSerializer(redisObjectMapper());

    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(ttl)
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration defaultConfig = buildCacheConfig(Duration.ofMinutes(10));

    RedisCacheConfiguration satelliteConfig = buildCacheConfig(Duration.ofMinutes(10));

    RedisCacheConfiguration constellationConfig = buildCacheConfig(Duration.ofMinutes(15));

    RedisCacheConfiguration satellitesListConfig = buildCacheConfig(Duration.ofMinutes(5));

    RedisCacheConfiguration constellationsListConfig = buildCacheConfig(Duration.ofMinutes(5));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withCacheConfiguration("satellite", satelliteConfig)
        .withCacheConfiguration("constellation", constellationConfig)
        .withCacheConfiguration("satellites", satellitesListConfig)
        .withCacheConfiguration("constellations", constellationsListConfig)
        .build();
  }

  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler() {
      @Override
      public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        System.err.printf("Redis GET error [cache=%s, key=%s]: %s%n",
            cache != null ? cache.getName() : "null", key, exception.getMessage());
      }

      @Override
      public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
          Object value) {
        System.err.printf("Redis PUT error [cache=%s, key=%s]: %s%n",
            cache != null ? cache.getName() : "null", key, exception.getMessage());
      }

      @Override
      public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        System.err.printf("Redis EVICT error [cache=%s, key=%s]: %s%n",
            cache != null ? cache.getName() : "null", key, exception.getMessage());
      }

      @Override
      public void handleCacheClearError(RuntimeException exception, Cache cache) {
        System.err.printf("Redis CLEAR error [cache=%s]: %s%n",
            cache != null ? cache.getName() : "null", exception.getMessage());
      }
    };
  }
}
