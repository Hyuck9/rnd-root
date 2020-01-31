package com.nexmore.rnd.common.exception;

import com.nexmore.rnd.common.domain.message.BaseMessage;

/**
 * RND 시스템 공통 Exception 처리
 */
public class RndSysException extends AbstractRndException {

	private static final long serialVersionUID = 4022434216187363891L;

	/**
	 * RND 시스템 공통 Exception
	 * @param status	에러 상태 코드
	 * @param reason	실패 사유
	 */
	public RndSysException(String status, String reason) {
		super(status, reason);
	}

	/**
	 * RND 시스템 공통 Exception
	 * @param status	에러 상태 코드
	 * @param reason	실패 사유
	 * @param cause		실패 원인
	 */
	public RndSysException(String status, String reason, Throwable cause) {
		super(status, reason, cause);
	}

	/**
	 * RND 시스템 공통 Exception
	 * @param status		에러 상태 코드
	 * @param reason		실패 사유
	 * @param rndMessage	RND 서비스 메시지
	 */
	public RndSysException(String status, String reason, BaseMessage rndMessage) {
		super(status, reason, rndMessage);
	}

	/**
	 * RND 시스템 공통 Exception
	 * @param status		에러 상태 코드
	 * @param reason		실패 사유
	 * @param rndMessage	RND 서비스 메시지
	 * @param cause			실패 원인
	 */
	public RndSysException(String status, String reason, BaseMessage rndMessage, Throwable cause) {
		super(status, reason, rndMessage, cause);
	}
}
