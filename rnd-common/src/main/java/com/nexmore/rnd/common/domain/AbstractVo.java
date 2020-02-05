package com.nexmore.rnd.common.domain;

import com.nexmore.rnd.common.util.RndReflectionToStringBuilder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 모든 도메인의 최상위 클래스
 */
@EqualsAndHashCode

public abstract class AbstractVo implements Serializable {

	private static final long serialVersionUID = 8410158049835528294L;

	@Override
	public String toString() {
		return new RndReflectionToStringBuilder(this).build();
	}

//	/**
//	 * 도메인에서 toString() 시 특정 필드들을 제외하고 출력
//	 * @param excludeFields	제외할 필드
//	 * @return	String
//	 */
//	public String toStringExcludeFields(final String... excludeFields) {
//		return new RndReflectionToStringBuilder(this).build();
//	}
}
