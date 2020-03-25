package com.qf.ithub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by Administrator on 2019/6/25.
 */
@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public RedisTemplate getRedisTeplate(@Autowired RedisConnectionFactory connectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        // RedisTemplate 的序列化的3个取值
        // new StringRedisSerializer()
        //NEW JdkSerializationRedisSerializer()  值被序列化成这样就好
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
