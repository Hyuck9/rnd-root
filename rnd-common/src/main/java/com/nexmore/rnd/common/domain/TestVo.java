package com.nexmore.rnd.common.domain;

import com.nexmore.rnd.common.domain.message.BaseMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class TestVo extends BaseMessage {

	private static final long serialVersionUID = 869152561931721905L;

	private String serviceId;
	private String message;
}
