package com.nexmore.rnd.common.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nexmore.rnd.common.domain.AbstractVo;
import lombok.Getter;
import lombok.Setter;

/**
 * MQ Message 최상위 Object
 */
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public abstract class BaseMessage extends AbstractVo {

	@JsonIgnore
	private MessageHeaders headers;

	@JsonIgnore
	private String messageId;
}
