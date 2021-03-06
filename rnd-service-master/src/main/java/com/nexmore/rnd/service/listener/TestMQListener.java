package com.nexmore.rnd.service.listener;

import com.nexmore.rnd.common.domain.TestVo;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.common.exception.AbstractRndException;
import com.nexmore.rnd.lib.rabbit.queue.QueueInterface;
import com.nexmore.rnd.lib.rabbit.queue.RouteKeyInterface;
import com.nexmore.rnd.lib.rabbit.util.RabbitListenerTemplate;
import com.nexmore.rnd.lib.rabbit.util.RabbitListenerTemplate.*;
import com.nexmore.rnd.service.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestMQListener {

	private final TestService testService;

	private final RabbitListenerTemplate rabbitListenerTemplate;

	public TestMQListener(TestService testService, RabbitListenerTemplate rabbitListenerTemplate) {
		this.testService = testService;
		this.rabbitListenerTemplate = rabbitListenerTemplate;
	}

	/**
	 * 테스트용 MQ 리스너
	 */
	@RabbitListener(containerFactory = "messageListenerContainerTest", queues = { QueueInterface.RND_SERVICE_TEST })
	public void handleTestMQListener(final TestVo requestVo, @Headers MessageHeaders headers) {
		rabbitListenerTemplate.excute(requestVo, headers, new ExecuteHandler() {
			@Override
			public void execute() {
				doServiceProcess(requestVo);
			}

			@Override
			public ErrorResponse errorResponse(AbstractRndException exception) {
				BaseMessage message = exception.getRndMessage();
				return new ErrorResponse(RouteKeyInterface.RND_SERVICE_TEST, message);
			}
		});
	}

	private void doServiceProcess(TestVo requestVo) {
		testService.startTest(requestVo);
	}

}
