package com.nexmore.rnd.batch.service;

import com.nexmore.rnd.batch.mapper.mariadb.MariaMapper;
import com.nexmore.rnd.batch.mapper.oracle.OracleMapper;
import com.nexmore.rnd.common.domain.TestVo;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.common.util.MessageUtil;
import com.nexmore.rnd.rabbit.queue.RouteKeyInterface;
import com.nexmore.rnd.rabbit.util.RabbitClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class TestService {

	private final MariaMapper mariaMapper;
	private final OracleMapper oracleMapper;
	private final RabbitClientTemplate rabbitTemplate;

	public TestService(MariaMapper mariaMapper, OracleMapper oracleMapper, RabbitClientTemplate rabbitTemplate) {
		this.mariaMapper = mariaMapper;
		this.oracleMapper = oracleMapper;
		this.rabbitTemplate = rabbitTemplate;
	}


	public void startBatch() {
		TestVo testVo = new TestVo();
		MessageUtil.initMessage(testVo);
		testVo.setMessage("TEST");
		testVo.setServiceId("TEST_SERVICE_ID");
		rabbitTemplate.convertAndSend(RouteKeyInterface.RND_SERVICE_TEST, testVo);
		log.debug(mariaMapper.getNow());
		log.debug(oracleMapper.getNow());
	}

}
