package com.nexmore.rnd.web.support;

import com.nexmore.rnd.common.domain.TestVo;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.common.domain.message.MessageHeaders;
import com.nexmore.rnd.common.util.MessageUtil;
import com.nexmore.rnd.lib.rabbit.util.RabbitClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 비동기 메서드 실행자
 */
@Slf4j
@Component
public class AsyncMethodInvoker {

	private final RabbitClientTemplate rabbitClientTemplate;

	private final DeferredResultHolder deferredResultHolder;

	public AsyncMethodInvoker(RabbitClientTemplate rabbitClientTemplate, DeferredResultHolder deferredResultHolder) {
		this.rabbitClientTemplate = rabbitClientTemplate;
		this.deferredResultHolder = deferredResultHolder;
	}

	/**
	 * 비동기 메서드 실행
	 * @param routingKey     라우팅키
	 * @param requestVo      요청객체
	 * @param deferredResult 응답객체
	 * @param request        클라이언트의 요청과 관련된 정보
	 */
	@Async
	public void execute(String routingKey, final BaseMessage requestVo, final DeferredResult<BaseMessage> deferredResult, HttpServletRequest request) {
		PendingReply pendingReply = new PendingReply(deferredResult);

		// key = messageId, value = PendingReply(DeferredResult<BaseMessage>)
		deferredResultHolder.set(requestVo.getHeaders().get(MessageHeaders.MESSAGE_ID).toString(), pendingReply);

		rabbitClientTemplate.convertAndSend(routingKey, requestVo);

		BaseMessage baseMessage = null;

		// TODO: 추후 response 관련 Base VO 생성
		TestVo response = new TestVo();

		deferredResult.onTimeout(() -> deferredResultHolder.remove(requestVo.getHeaders().get(MessageHeaders.MESSAGE_ID).toString()));

		try {
		//			// TODO : Async Response Queue Timeout 설정
		//			if(request.getRequestURI().startsWith("/skt/lbs/cg")) {
		//				// 자녀안심에 대한 Response Wait
						baseMessage = pendingReply.getQueue().poll(320 * 1000, TimeUnit.MILLISECONDS);
		//				response = new ResponseChildGuardVo();
		//			} else if(request.getRequestURI().startsWith("/skt/lbs/cgp")) {
		//				// 자녀안심플러스에 대한 Response Wait
		//				baseMessage = rendingReply.getQueue().poll(320 * 1000, TimeUnit.MILLISECONDS);
		//				response = new ResponseChildGuardPlusVo();
		//			} else if(request.getRequestURI().startsWith("/skt/lbs/ikids")) {
		//				// 아이키즈에 대한 Response Wait
		//				baseMessage = rendingReply.getQueue().poll(320 * 1000, TimeUnit.MILLISECONDS);
		//				response  = new ResponseIKidsVo();
		//			} else if(request.getRequestURI().startsWith("/skt/lbs/safe")) {
		//				// 아이키즈에 대한 Response Wait
		//				baseMessage = rendingReply.getQueue().poll(320 * 1000, TimeUnit.MILLISECONDS);
		//				response  = new ResponseIKidsVo();
		//			}
		} catch (Exception e) {
		//			exThrown = ex;
			log.error(e.getMessage(), e);
		}

		if ( deferredResult.isSetOrExpired() ) {
			return;
		}

		if ( baseMessage == null ) {
			MessageUtil.copyHeaders(requestVo, response);
//			response.setReturnCode(ExceptionConstant.EXCEPTION_CODE_CONNECTION);	TODO: 추후 response 용 return code 생성
			baseMessage = response;
		}

		deferredResult.setResult(baseMessage);

		deferredResultHolder.remove(requestVo.getHeaders().get(MessageHeaders.MESSAGE_ID).toString());
	}
}
