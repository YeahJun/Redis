package yeahjun.lowest.redis.config;

import yeahjun.lowest.redis.model.TestVo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
@RequiredArgsConstructor
public class RedisConfig {
        private final RedisProperties redisProperties;

        @Bean
        public LettuceConnectionFactory connectionFactory() {

            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(redisProperties.getSentinel().getMaster());
            redisProperties.getSentinel().getNodes().forEach(s -> sentinelConfig.sentinel(s, redisProperties.getPort()));

            LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(sentinelConfig);

            return connectionFactory;
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {

            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(TestVo.class);

            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(connectionFactory());
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

            return redisTemplate;
        }

}
