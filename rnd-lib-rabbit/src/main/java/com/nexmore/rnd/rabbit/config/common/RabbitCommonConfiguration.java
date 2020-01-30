package com.nexmore.rnd.rabbit.config.common;

import com.nexmore.rnd.common.annotation.SelectedConfiguration;
import com.nexmore.rnd.common.config.PropertyPlaceHolderConfiguration;
import com.nexmore.rnd.rabbit.message.converter.RndMessageConverter;
import com.nexmore.rnd.rabbit.queue.RouteKeyInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
@SelectedConfiguration
@Import(PropertyPlaceHolderConfiguration.class)
public class RabbitCommonConfiguration {

	/** MQ 주소 */
	@Value("${mq.addresses:localhost:15672}")
	String addresses;

	/** user name */
	@Value("${mq.username:admin}")
	String username;

	/** password */
	@Value("${mq.password:admin}")
	String password;

	/** 초기 간격 값 */
	@Value("${mq.retry.initialInterval:500}")
	long initialInterval;

	/** 승수 */
	@Value("${mq.retry.multiplier:10.0}")
	double multiplier;

	/** max 간격 값 */
	@Value("${mq.retry.maxInterval:10000}")
	long maxInterval;

	/**
	 * RabbitMQ 연결 및 관리 Interface
	 * @return CachingConnectionFactory (Spring 에서 제공하는 ConnectionFactory 객체)
	 */
	@Bean
	public ConnectionFactory rabbitConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(addresses);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}

	@Bean
	public MessageConverter rabbitMessageConverter() {
		return new RndMessageConverter();
	}

	/**
	 * Event 를 Publishing 하는 template 생성
	 * @return RabbitTemplate
	 */
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
		template.setMessageConverter(rabbitMessageConverter());
		template.setExchange(RouteKeyInterface.EXCHANGE_COMMON);

		// 추후 정의 (현재는 default 값)
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(initialInterval);
		backOffPolicy.setMultiplier(multiplier);
		backOffPolicy.setMaxInterval(maxInterval);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		template.setRetryTemplate(retryTemplate);
		return template;
	}

}
