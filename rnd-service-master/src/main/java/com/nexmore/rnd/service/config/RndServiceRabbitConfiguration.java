package com.nexmore.rnd.service.config;

import com.nexmore.rnd.lib.rabbit.config.TestConsumerConfiguration;
import com.nexmore.rnd.lib.rabbit.config.common.RabbitCommonConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Rabbit MQ 설정 등록
 */
@Configuration
@Import({
		RabbitCommonConfiguration.class,
		TestConsumerConfiguration.class
})
public class RndServiceRabbitConfiguration {
}
