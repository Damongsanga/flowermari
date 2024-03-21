package com.ssafy.maryflower.infrastructure.config;

import com.ssafy.maryflower.bouquet.service.DataSubscribeService;
import com.ssafy.maryflower.infrastructure.RedisMessageListener;
import com.ssafy.maryflower.infrastructure.TestRedisMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisMessageListener redisMessageListener;
    private final TestRedisMessageListener testRedisMessageListener;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(password);

        return new LettuceConnectionFactory(configuration);
    }

    // Redis 작업을 위한 RedisTemplate 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }


    // Redis 메시지 리스너 어댑터 설정
    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(redisMessageListener);
    }


    // test 용.
    @Bean
    MessageListenerAdapter TestmessageListenerAdapter() {
        return new MessageListenerAdapter(testRedisMessageListener);
    }

    // Redis 메시지 리스너 컨테이너 설정
    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListenerAdapter(), Topic1());
        // 테스트 용
        container.addMessageListener(TestmessageListenerAdapter(),TestTopic());
        return container;
    }

    // Redis pub/sub 토픽 설정
    @Bean
    ChannelTopic Topic1() {
        return new ChannelTopic("ch2");
    }

    // test용
    @Bean
    ChannelTopic TestTopic() {
        return new ChannelTopic("test");
    }


}
