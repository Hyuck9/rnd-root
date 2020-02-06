package com.nexmore.rnd.web.support;

import com.google.common.collect.Queues;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import lombok.Getter;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class PendingReply {

	private DeferredResult<BaseMessage> deferredResult;

	private final LinkedBlockingQueue<BaseMessage> queue = Queues.newLinkedBlockingQueue(1);

	/**
	 * Async long polling 후 결과값을 반환
	 * @param deferredResult	결과값
	 */
	public PendingReply(DeferredResult<BaseMessage> deferredResult) {
		super();
		this.deferredResult = deferredResult;
	}
}
