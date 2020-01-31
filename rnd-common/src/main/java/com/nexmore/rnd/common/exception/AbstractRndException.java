package com.nexmore.rnd.common.exception;

import com.nexmore.rnd.common.domain.code.ExceptionConstant;
import com.nexmore.rnd.common.domain.message.BaseMessage;

public abstract class AbstractRndException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 상태 코드
	 */
	private String status = String.valueOf(ExceptionConstant.EXCEPTION_CODE_UNKNOWN);

	/**
	 * 실패 사유
	 */
	private String reason;

	/**
	 * Message Object
	 */
	private BaseMessage rndMessage;

	/**
	 * LBS 공통 Exception 추상화 메소드
	 * @param status		상태 코드
	 * @param reason		실패 사유
	 */
	protected AbstractRndException(String status, String reason) {
		super(status + " : " + reason);
		this.status = status;
		this.reason = reason;
	}

	/**
	 * RND 공통 Exception 추상화 메소드
	 * @param status		상태 코드
	 * @param reason		실패 사유
	 * @param cause			실패 원인
	 */
	protected AbstractRndException(String status, String reason, Throwable cause) {
		super(status + " : " + reason, cause);
		this.status = status;
		this.reason = reason;
	}

	/**
	 * LBS 공통 Exception 추상화 메소드
	 * @param status		상태 코드
	 * @param reason		실패 사유
	 * @param rndMessage	RND 메시지
	 */
	protected AbstractRndException(String status, String reason, BaseMessage rndMessage) {
		super(status + " : " + reason);
		this.status = status;
		this.reason = reason;
		this.rndMessage = rndMessage;
	}

	/**
	 * LBS 공통 Exception 추상화 메소드
	 * @param status		상태 코드
	 * @param reason		실패 사유
	 * @param rndMessage	RND 메시지
	 * @param cause			실패 원인
	 */
	protected AbstractRndException(String status, String reason, BaseMessage rndMessage, Throwable cause) {
		super(status + " : " + reason, cause);
		this.status = status;
		this.reason = reason;
		this.rndMessage = rndMessage;
	}

	public String getStatus() {
		return status;
	}

	public String getReason() {
		return reason;
	}

	public BaseMessage getRndMessage() {
		return rndMessage;
	}
}
