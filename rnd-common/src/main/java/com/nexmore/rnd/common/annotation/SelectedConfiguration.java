package com.nexmore.rnd.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

/**
 * 이 설정은 디폴트로 로딩되지 않으며,
 * 참조해 사용하는 애플리케이션 쪽에서 명시적으로 로딩해야 한다.
 * {@link org.springframework.context.annotation.Import} 애노테이션 참고
 *
 * @author HyunSub Shim
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
public @interface SelectedConfiguration {
    String value() default "";
}
