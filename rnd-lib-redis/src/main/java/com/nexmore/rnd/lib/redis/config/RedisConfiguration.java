package com.nexmore.rnd.lib.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Value("${spring.redis.host:192.168.0.94}")
	String host;

	@Value("${spring.redis.port:6379}")
	int port;

	@Value("${spring.redis.password:nex147200}")
	String password;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
		redisStandaloneConfiguration.setPassword(password);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory());
		stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
		return stringRedisTemplate;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> objectRedisTemplate = new RedisTemplate<>();
		objectRedisTemplate.setKeySerializer(new StringRedisSerializer());
		objectRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
		objectRedisTemplate.setConnectionFactory(redisConnectionFactory());
		return objectRedisTemplate;
	}

}
