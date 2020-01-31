package com.nexmore.rnd.common.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class StringUtil {

	/** 메시지 아이디 구성 분리문자	 */
	public static final String MESSAGE_ID_SPLIT = "-";

	/** 현재 시간을 가져오는 함수
	 * @param datePattern	Date format ex) yyyyMMddHHmmssSSS
	 * @return 시간 문자열
	 */
	public static String getNowDateString(String datePattern) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern));
	}

	/**
	 * 시스템 hostname 조회
	 * @return hostname
	 */
	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.error(e.getMessage(), e);
			return "RND";
		}
	}

	/**
	 * RND 공통 메시지 ID 생성
	 * @return 메시지 ID
	 */
	public static String getMessageId() {
		return getHostname() + MESSAGE_ID_SPLIT + getNowDateString("yyyyMMddHHmmss")+ MESSAGE_ID_SPLIT + UUID.randomUUID().toString().replace("-", "");

	}

}
