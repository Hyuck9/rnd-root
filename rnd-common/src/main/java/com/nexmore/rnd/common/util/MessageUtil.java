package com.nexmore.rnd.common.util;

import com.nexmore.rnd.common.annotation.MessageHeader;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.common.domain.message.MessageHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * BaseMessage 객체 설정용 유틸
 */
@Slf4j
public class MessageUtil {

	public static <T extends BaseMessage> T initMessage(T message) {
		MessageHeaders headers = new MessageHeaders();
		headers.put(MessageHeaders.MESSAGE_ID, StringUtil.getMessageId());
		headers.put(MessageHeaders.REQUEST_TIME, LocalDateTime.now());
		headers.put(MessageHeaders.PROCESS_COMPONENT_NAME, "TEST");

		syncHeader(message, headers);

		message.setHeaders(headers);
		message.setMessageId( String.valueOf(headers.get(MessageHeaders.MESSAGE_ID)) );

		return message;
	}

	/**
	 * Base 메시지에 MessageHeaders Annotation 으로 선언된 항목에 대하여 헤더메시지로 복사를 수행
	 * @param message		Base 메시지
	 * @param headers		Base 메시지 추출된 메시지 헤더
	 */
	public static void syncHeader(BaseMessage message, MessageHeaders headers) {
		for ( Field field : FieldUtils.getAllFields(message.getClass()) ) {
			MessageHeader messageHeader = field.getAnnotation(MessageHeader.class);
			if ( messageHeader == null ) {
				continue;
			}
			try {
				headers.put(messageHeader.name(), FieldUtils.readField(field, message, true));
			} catch (IllegalAccessException e) {
				log.warn(e.getMessage(), e);
			}
		}
	}
}
