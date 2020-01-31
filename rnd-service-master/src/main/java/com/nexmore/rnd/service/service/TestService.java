package com.nexmore.rnd.service.service;

import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.service.mapper.mariadb.MariaMapper;
import com.nexmore.rnd.service.mapper.oracle.OracleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 테스트용 서비스 로직
 * 테스트 리스너에서 받은 요청 정보에 대한 비즈니스 로직
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class TestService {

	private final MariaMapper mariaMapper;
	private final OracleMapper oracleMapper;

	public TestService(MariaMapper mariaMapper, OracleMapper oracleMapper) {
		this.mariaMapper = mariaMapper;
		this.oracleMapper = oracleMapper;
	}

	public void startTest(BaseMessage message) {
		log.debug(mariaMapper.getNow());
		log.debug(oracleMapper.getNow());
		log.debug(message.toString());
	}

}
