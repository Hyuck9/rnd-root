package com.nexmore.rnd.batch.service;

import com.nexmore.rnd.batch.mapper.mariadb.MariaMapper;
import com.nexmore.rnd.batch.mapper.oracle.OracleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


	public void startBatch() {
		log.debug(mariaMapper.getNow());
		log.debug(oracleMapper.getNow());
	}

}
