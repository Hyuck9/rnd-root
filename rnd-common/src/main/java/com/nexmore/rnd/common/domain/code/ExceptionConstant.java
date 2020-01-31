package com.nexmore.rnd.common.domain.code;

/**
 * API 공통 Exception Code
 */
public interface ExceptionConstant {

	/** Exception 코드 - unknown */
	int EXCEPTION_CODE_UNKNOWN = 1000;

	/** Exception 코드 - DB */
	int EXCEPTION_CODE_DB_ERROR = 1001;

	/** Exception 코드 - PARAMETER */
	int EXCEPTION_CODE_PARAMETER_ERROR = 1002;

	/** Exception 코드 - runtime */
	int EXCEPTION_CODE_RUNTIME_ERROR = 1003;

	/** Exception 코드 - response timeout */
	int EXCEPTION_CODE_RESPONSE_TIMEOUT = 1004;

	/** Exception 코드 - 운영중 timeout */
	int EXCEPTION_CODE_OPERATION_TIMEOUT = 1005;

	/** Exception 코드 - duplication */
	int EXCEPTION_CODE_DUPLICATION_ERROR = 1006;

	/** Exception 코드 - RECORD_NOT_FOUND */
	int EXCEPTION_CODE_RECORD_NOT_FOUND = 1007;

}
