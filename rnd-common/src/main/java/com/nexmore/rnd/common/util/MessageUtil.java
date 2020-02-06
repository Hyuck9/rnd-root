package com.nexmore.rnd.common.util;

import com.nexmore.rnd.common.annotation.MessageHeader;
import com.nexmore.rnd.common.domain.code.ExceptionConstant;
import com.nexmore.rnd.common.domain.message.BaseMessage;
import com.nexmore.rnd.common.domain.message.MessageHeaders;
import com.nexmore.rnd.common.exception.RndSysException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * BaseMessage 객체 설정용 유틸
 */
@Slf4j
public class MessageUtil {

	public static <T extends BaseMessage> void initMessage(T message) {
		MessageHeaders headers = new MessageHeaders();
		headers.put(MessageHeaders.MESSAGE_ID, StringUtil.getMessageId());
		headers.put(MessageHeaders.REQUEST_TIME, LocalDateTime.now());
		headers.put(MessageHeaders.PROCESS_COMPONENT_NAME, "TEST");

		syncHeader(message, headers);

		message.setHeaders(headers);
		message.setMessageId( String.valueOf(headers.get(MessageHeaders.MESSAGE_ID)) );
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

	/**
	 * 컴포넌트에 대한 ID 를 조회
	 * @return	컴포넌트 ID
	 */
	public static String getComponentId() {
		return Objects.toString(System.getProperty("rnd.conf.component.id"), "N/A");
	}

	/**
	 * Header 를 복사
	 * @param src  소스 객체
	 * @param dest 목적지 객체
	 */
	public static void copyHeaders(BaseMessage src, BaseMessage dest) {
		try {
			if ( src == null || dest == null ) {
				return;
			}
			MessageHeaders srcHeaders = src.getHeaders() == null ? new MessageHeaders() : src.getHeaders();
			MessageHeaders destHeaders = copyHeader(srcHeaders);
			destHeaders.put(MessageHeaders.PROCESS_COMPONENT_NAME, getComponentId());
			dest.setHeaders(destHeaders);
		} catch (Exception e) {
			throw new RndSysException(String.valueOf(ExceptionConstant.EXCEPTION_CODE_UNKNOWN), "thrown exception when message copy.", e);
		}
	}

	/**
	 * 메시지 헤더를 복사하여 또 다른 객체로 리턴
	 * @param org 원본 메시지 헤더 객체
	 * @return	  복사된 메시지 헤더 객체
	 */
	public static MessageHeaders copyHeader(MessageHeaders org) {
		if ( MapUtils.isEmpty(org) ) {
			return new MessageHeaders();
		}

		MessageHeaders copied = new MessageHeaders();

		for ( Map.Entry<String, Object> entry : org.entrySet() ) {
			copied.put(entry.getKey(), entry.getValue());
		}

		return copied;
	}
}
