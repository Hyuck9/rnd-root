package com.nexmore.rnd.common.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 모든 도메인의 최상위 클래스
 */
@ToString
@EqualsAndHashCode
public abstract class AbstractVo implements Serializable {

	private static final long serialVersionUID = 8410158049835528294L;

	// toStringExcludeFields 등 구현 해야할지 고민중
}
