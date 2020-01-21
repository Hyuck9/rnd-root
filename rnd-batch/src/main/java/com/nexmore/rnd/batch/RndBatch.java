package com.nexmore.rnd.batch;

import com.nexmore.rnd.batch.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component("rndBatch")
public class RndBatch {

	private final TestService testService;

	RndBatch(TestService testService) {
		this.testService = testService;
	}

	@PostConstruct
	public void initialize(){
		testService.startBatch();
	}

	public void startBatch() {
		log.debug("스케줄 배치를 시작합니다.");

		testService.startBatch();

		log.debug("스케줄 배치를 종료합니다.");
	}

}
