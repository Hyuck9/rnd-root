package com.nexmore.rnd.lib.rabbit.message.converter;

import com.nexmore.rnd.common.domain.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

@SuppressWarnings("NullableProblems")
@Slf4j
public class RndMessageConverter extends SimpleMessageConverter {


	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		Object object = super.fromMessage(message);
		BaseMessage result = null;

		if ( object instanceof BaseMessage ) {
			result = (BaseMessage) object;
		}

		if ( result == null ) {
			return null;
		}

		if ( StringUtils.isEmpty(result.getMessageId()) ) {
			result.setMessageId( message.getMessageProperties().getCorrelationId() );
		}

		log.debug("Processing message from consumer queue name [ {} ]!", message.getMessageProperties().getConsumerQueue());
		log.debug("Processing message from received route key [ {} ]!", message.getMessageProperties().getReceivedRoutingKey());
		log.debug("Processing message from getMessageId [ {} ]!", result.getMessageId());
		log.debug("Processing message from correlationId [ {} ]!", message.getMessageProperties().getCorrelationId());

		return result;
	}

	@Override
	protected Message createMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		Message result = super.createMessage(object, messageProperties);
		BaseMessage baseMessage = null;

		if ( object instanceof BaseMessage ) {
			baseMessage = (BaseMessage) object;
		}

		if ( baseMessage == null ) {
			return null;
		}

		log.debug("Processing message from getMessageId [ {} ]!", baseMessage.getMessageId());
		if ( baseMessage.getMessageId() != null ) {
			result.getMessageProperties().setCorrelationId( baseMessage.getMessageId() );
			log.debug("Processing message from correlationId [ {} ]!", result.getMessageProperties().getCorrelationId());
		}

		return result;
	}
}
