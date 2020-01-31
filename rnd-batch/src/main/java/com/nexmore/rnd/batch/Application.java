package com.nexmore.rnd.batch;

import com.nexmore.rnd.common.annotation.RndDaemonApplication;
import com.nexmore.rnd.rabbit.config.common.RabbitCommonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@RndDaemonApplication
@Import({RabbitCommonConfiguration.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
