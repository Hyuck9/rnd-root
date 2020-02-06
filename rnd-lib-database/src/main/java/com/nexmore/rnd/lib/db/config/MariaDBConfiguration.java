package com.nexmore.rnd.lib.db.config;

import com.nexmore.rnd.lib.db.annotation.RndMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Slf4j
@Configuration
@MapperScan(basePackages = "com.nexmore.rnd.*.mapper.mariadb", annotationClass = RndMapper.class, sqlSessionFactoryRef = "mariadbSqlSessionFactory")
public class MariaDBConfiguration {

	@Value("${mariadb.datasource.driverClassName:org.mariadb.jdbc.Driver}")
	private String driverClassName;

	@Value("${mariadb.datasource.url:jdbc:mariadb://58.229.105.141:3307/datainfo}")
	private String url;

	@Value("${mariadb.datasource.username:datainfo}")
	private String username;

	@Value("${mariadb.datasource.password:nex147200}")
	private String password;

	@Value("${mariadb.datasource.validation-query}")
	private String validationQuery;

	@Value("${mariadb.datasource.test-on-borrow}")
	private boolean borrow;


	@Bean
	@Primary
	public BasicDataSource mariadbDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestOnBorrow(borrow);
		return dataSource;
	}

	@Bean
	@Primary
	public SqlSessionFactoryBean mariadbSqlSessionFactory(@Qualifier("mariadbDataSource") BasicDataSource dataSource) throws Exception {
		dataSource.setMaxIdle(3);
		dataSource.setMinIdle(3);
		dataSource.setMaxTotal(3);
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage("com.nexmore.rnd.domain");
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(
						"classpath*:mapper/mariadb/*.xml"
				)
		);
		return sessionFactory;
	}

	@Bean
	@Primary
	public SqlSessionTemplate mariadbSessionTemplate(@Qualifier("mariadbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
