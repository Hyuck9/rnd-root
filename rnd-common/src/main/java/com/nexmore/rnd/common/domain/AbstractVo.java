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
	// toStringExcludeFields 등 구현 해야할지 고민중
}
