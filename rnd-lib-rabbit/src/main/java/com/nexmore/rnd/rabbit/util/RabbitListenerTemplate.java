package com.nexmore.rnd.rabbit.util;

import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.common.domain.message.MessageHeaders;
import com.nexmore.rnd.common.exception.AbstractRndException;
import com.nexmore.rnd.common.exception.RndBizException;
import com.nexmore.rnd.common.exception.RndSysException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitListenerTemplate {

	private final RabbitTemplate rabbitTemplate;

	public RabbitListenerTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public <T> void excute(BaseMessage request, MessageHeaders headers, ExecuteHandler handler) {
		// TODO: trace log 작성 -> RabbitTraceLogger.trace(flow, Status.IN, Objects.toString(headers.get("amqp_consumerQueue"), "N/A"), request);

		Exception exception = null;

		try {
			handler.execute();
		} catch (Exception e) {
			exception = e;
			log.error(e.getMessage(), e);
		}

		if ( exception == null ) {
			return;
		}

		AbstractRndException rndEx = transformIfNotRndException(exception);

		try {
			ErrorResponse errorResponse = handler.errorResponse(rndEx);

			if ( errorResponse != null && errorResponse.getBaseMessage() != null ) {
				this.rabbitTemplate.convertAndSend(errorResponse.getRouteKey(), errorResponse.getBaseMessage());
				// TODO: trace log 작성
			} else {
				// TODO: trace log 작성
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	private AbstractRndException transformIfNotRndException(Exception exception) {
		if ( exception instanceof RndBizException) {
			return (RndBizException) exception;
		} else if ( exception instanceof RndSysException) {
			return (RndSysException) exception;
		} else {
			return new RndSysException("UNKNOWN_ERROR", "예상치 못한 예외가 발생했습니다.", null, exception);
		}
	}

	/**
	 * 에러 응답 클래스
	 */
	@Data
	public static class ErrorResponse {
		private String routeKey;
		private BaseMessage baseMessage;
	}

	/**
	 * 수행 핸들러 인터페이스
	 */
	public interface ExecuteHandler {
		/**
		 * 수행 메서드
		 */
		void execute();

		/**
		 * 에러 응답
		 * @param exception Exception
		 * @return	에러 응답 도메인
		 */
		ErrorResponse errorResponse(AbstractRndException exception);
	}
}
