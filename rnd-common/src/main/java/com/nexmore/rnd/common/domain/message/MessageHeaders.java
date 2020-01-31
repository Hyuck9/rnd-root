package com.nexmore.rnd.common.domain.message;

import java.util.HashMap;

/**
 * MessageHeaders Key 값 정의
 */
public class MessageHeaders extends HashMap<String, Object> {

	/** 메시지 ID */
	public final static String MESSAGE_ID 				= "messageId";
	/** 요청 시간 */
	public final static String REQUEST_TIME             = "requestTime";
	/** 처리 컴포넌트명 */
	public final static String PROCESS_COMPONENT_NAME   = "processComponentName";

}
