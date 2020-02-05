package com.nexmore.rnd.common.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nexmore.rnd.common.domain.AbstractVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * MQ Message 최상위 Object
 */
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public abstract class BaseMessage extends AbstractVo {

	private static final long serialVersionUID = -1803092029955861057L;

	@JsonIgnore
	private MessageHeaders headers;

	@JsonIgnore
	private String messageId;
}
