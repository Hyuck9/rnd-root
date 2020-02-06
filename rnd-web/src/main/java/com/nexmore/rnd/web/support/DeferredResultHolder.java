package com.nexmore.rnd.web.support;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeferredResultHolder {

	private Map<String, PendingReply> pendingMap = Maps.newConcurrentMap();

	/**
	 * 비동기 호출간 메시지 Key 에 대한 결과값 설정
	 * @param key    비동기 호출간 메시지 Key
	 * @param result 메시지 Key 에 대한 결과 값
	 */
	public void set(String key, PendingReply result) {
		this.pendingMap.put(key, result);
	}

	/**
	 * 비동기 호출간 메시지 Key 에 대한 조회 및 삭제
	 * @param key 비동기 호출간 메시지 Key
	 * @return	  메시지 Key 에 대한 결과 값
	 */
	public PendingReply getAndRemove(String key) {
		PendingReply result = this.pendingMap.get(key);
		this.pendingMap.remove(key);
		return result;
	}

	/**
	 * 비동기 호출간 메시지 삭제
	 * @param key 삭제할 메시지 Key
	 */
	public void remove(String key) {
		this.pendingMap.remove(key);
	}

}
