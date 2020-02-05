package com.nexmore.rnd.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 문자열 동기화 클래스
 * @see org.apache.commons.lang3.builder.ReflectionToStringBuilder
 */
public class RndReflectionToStringBuilder extends ReflectionToStringBuilder {

	public RndReflectionToStringBuilder(Object object) {
		super(object);
		excludeFieldNames = findExcludeFields();
	}

	/**
	 * 제외 필드 검색
	 * @return String[] 제외 필드 배열
	 */
	private String[] findExcludeFields() {
		List<String> excludeFieldNames = Lists.newArrayList("messageId");

//		추후 제외할 필드 excludeFieldNames 에 add
//		for ( Field field : getObject().getClass().getDeclaredFields() ) {
//			SecureField secureField = field.getAnnotation(SecureField.class);
//			if(secureField == null) {
//				continue;
//			}
//			if(!secureField.logable()) {
//				excludedFieldNames.add(field.getName());
//			}
//		}

		return excludeFieldNames.toArray(new String[0]);
	}
}
