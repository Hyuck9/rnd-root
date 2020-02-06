package com.nexmore.rnd.lib.rabbit.config;

import com.nexmore.rnd.common.annotation.SelectedConfiguration;
import com.nexmore.rnd.lib.rabbit.config.common.RabbitCommonConfiguration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SelectedConfiguration
@Import(RabbitCommonConfiguration.class)
@EnableRabbit
public class TestConsumerConfiguration {

	@Value("${mq.test.listener.minConcurrentConsumers:1}")
	int minConcurrentConsumers;

	@Value("${mq.test.listener.maxConcurrentConsumers:50}")
	int maxConcurrentConsumers;

	private final ConnectionFactory rabbitConnectionFactory;

	public TestConsumerConfiguration(ConnectionFactory rabbitConnectionFactory) {
		this.rabbitConnectionFactory = rabbitConnectionFactory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory messageListenerContainerTest() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setConcurrentConsumers(minConcurrentConsumers);
		factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
		return factory;
	}
}
