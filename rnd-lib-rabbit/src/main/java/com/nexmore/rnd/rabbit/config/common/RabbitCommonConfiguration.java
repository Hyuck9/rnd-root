package com.nexmore.rnd.rabbit.config.common;

import com.nexmore.rnd.common.annotation.SelectedConfiguration;
import com.nexmore.rnd.common.config.PropertyPlaceHolderConfiguration;
import com.nexmore.rnd.rabbit.queue.RouteKeyInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

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

	@Bean
	ConnectionFactory rabbitConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(addresses);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}


	@Bean
	RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
		template.setExchange(RouteKeyInterface.EXCHANGE_COMMON);

		return template;
	}

}
