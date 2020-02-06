package com.nexmore.rnd.lib.rabbit.queue;

/**
 * RabbitMQ route key 정의
 */
public interface RouteKeyInterface {

	/** EXCHANGE COMMON */
	String EXCHANGE_COMMON = "rnd.exchange.topic";

	/** 테스트용 */
	String RND_SERVICE_TEST = "rnd.service.test.msg";

}
