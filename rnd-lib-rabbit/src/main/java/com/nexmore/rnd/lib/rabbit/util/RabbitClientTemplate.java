package com.nexmore.rnd.lib.rabbit.util;

import com.google.common.base.Throwables;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitClientTemplate {

	private final RabbitTemplate rabbitTemplate;

	/** Default Reply Timeout */
	private static final long DEFAULT_REPLY_TIMEOUT = 5000L;

	public RabbitClientTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void convertAndSend(String routingKey, BaseMessage baseMessage) {
		Exception exception = null;

		try {
			rabbitTemplate.convertAndSend(routingKey, baseMessage);
		} catch (Exception e) {
			exception = e;
			log.error(e.getMessage(), e);
		}

		if ( exception != null ) {
			Throwables.throwIfInstanceOf(exception, RuntimeException.class);
			// TODO: throw new LbsSysException(String.valueOf(ExceptionConstant.EXCEPTION_CODE_RUNTIME_ERROR), "thrown unknown exception.", exThrown);
		} else {
			// TODO: trace log 작성
		}
	}

	/**
	 * Direct Reply-To
	 * Reply Timeout 이 발생할 경우
	 * Response Timeout Runtime Exception 발생
	 * @param routingKey routingKey
	 * @param object message
	 * @return <T>
	 */
	public <T> T convertSendAndReceive(String routingKey, Object object) {
		return this.convertSendAndReceive(routingKey, object, DEFAULT_REPLY_TIMEOUT);
	}

	/**
	 * Direct Reply-To
	 * 사용자 임의 ReplyTimeout 설정 가능
	 * @param routingKey routingKey
	 * @param object message
	 * @param replyTimeout Reply Timeout
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T> T convertSendAndReceive(String routingKey, Object object, long replyTimeout) {
		Exception exception = null;
		T receiveObject = null;

		try {
			rabbitTemplate.setReplyTimeout(replyTimeout);
			receiveObject = (T) rabbitTemplate.convertSendAndReceive(routingKey, object);
		} catch (Exception e) {
			exception = e;
			log.error(e.getMessage(), e);
		}

		if ( exception != null ) {
			Throwables.throwIfInstanceOf(exception, RuntimeException.class);
			// TODO: throw new LbsSysException(String.valueOf(ExceptionConstant.EXCEPTION_CODE_RUNTIME_ERROR), "thrown unknown exception.", exThrown);
		} else if ( receiveObject == null ) {
			Throwables.throwIfInstanceOf(exception, RuntimeException.class);	// exception 이 항상 null 이므로 로직 수정해야 할듯
			// TODO: throw new LbsSysException(String.valueOf(ExceptionConstant.EXCEPTION_CODE_RUNTIME_ERROR), "thrown response timeout exception.", exThrown);
		} else {
			// TODO: trace log 작성
		}

		return receiveObject;
	}

}
