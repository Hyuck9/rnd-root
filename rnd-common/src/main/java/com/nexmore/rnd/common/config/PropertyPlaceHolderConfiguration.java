package com.nexmore.rnd.common.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 스프링 컨텍스트 영역에서 {@link org.springframework.beans.factory.annotation.Value}
 * 애노테이션을 사용한 프로퍼티 주입 기능을 제공한다.
 * 로딩할 프로퍼티 파일은 설정된 스프링 프로파일(spring.profiles.active)에 따라 결정한다.
 *
 * @author 조신득
 * @see PropertySourcesPlaceholderConfigurer
 * @see Profile
 * @since 1.0
 */
@Configuration
public class PropertyPlaceHolderConfiguration {

	/**
	 * Spring propertyPlaceholderConfigurer에 대한 Bean
	 *
	 * @throws IOException IO Exception
	 * @return propertyPlaceholderConfigurer 객체
	 */
	@Bean(name = "propertyPlaceholderConfigurer")
	public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() throws IOException {

		String configExternalized = Objects.toString(System.getProperty("rnd.config.externalized"), "false");

		String commonConfigHome = System.getProperty("rnd.conf.common.home");

		String componentConfHome = System.getProperty("rnd.conf.component.home");

		PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

		List<Resource> resources = Lists.newArrayList();

		//외부설정
		if (BooleanUtils.isTrue(Boolean.valueOf(configExternalized))) {
			Preconditions.checkArgument(StringUtils.isNoneBlank(commonConfigHome), "외부설정로드를 위한 공통프로퍼티위치 JVM프로퍼티가 존재하지 않습니다.");
			Preconditions.checkArgument(StringUtils.isNoneBlank(componentConfHome), "외부설정로드를 위한 컴포넌트프로퍼티위치 JVM프로퍼티가 존재하지 않습니다.");


			//플랫폼 공통 프로퍼티 목록
			resources.addAll(Arrays.asList(new PathMatchingResourcePatternResolver().getResources("file:" + commonConfigHome + "/prop/*.properties")));

			//컴포넌트 독점 프로퍼티 목록
			resources.addAll(Arrays.asList(new PathMatchingResourcePatternResolver().getResources("file:" + componentConfHome + "/prop/*.properties")));

		} else {
			//내부설정
			resources.addAll(Arrays.asList(new PathMatchingResourcePatternResolver().getResources("classpath*:prop/*.properties")));
		}

		propertyPlaceholderConfigurer.setLocations(resources.toArray(new Resource[0]));

		return propertyPlaceholderConfigurer;
	}
}
