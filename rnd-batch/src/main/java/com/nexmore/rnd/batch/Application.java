package com.nexmore.rnd.batch;

import com.nexmore.rnd.common.annotation.RndDaemonApplication;
import org.springframework.boot.SpringApplication;

@RndDaemonApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
