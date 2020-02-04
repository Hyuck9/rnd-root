package com.nexmore.rnd.db.config;

import com.nexmore.rnd.db.annotation.RndMapper;
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Slf4j
@Configuration
@MapperScan(basePackages = "com.nexmore.rnd.*.mapper.oracle", annotationClass = RndMapper.class, sqlSessionFactoryRef = "oracleSqlSessionFactory")
public class OracleConfiguration {

	@Value("${oracle.datasource.driverClassName:org.mariadb.jdbc.Driver}")
	private String driverClassName;

	@Value("${oracle.datasource.url:jdbc:mariadb://58.229.105.141:3307/datainfo}")
	private String url;

	@Value("${oracle.datasource.username:datainfo}")
	private String username;

	@Value("${oracle.datasource.password:nex147200}")
	private String password;

	@Value("${oracle.datasource.validation-query}")
	private String validationQuery;

	@Value("${oracle.datasource.test-on-borrow}")
	private boolean borrow;

	@Bean
	public BasicDataSource oracleDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setTestOnBorrow(borrow);
		dataSource.setValidationQuery(validationQuery);
		return dataSource;
	}

	@Bean(name = "oracleSqlSessionFactory")
	public SqlSessionFactoryBean oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(
						"classpath*:mapper/oracle/*.xml"
				)
		);
		return sessionFactory;
	}

	@Bean
	public SqlSessionTemplate oracleSessionTemplate(@Qualifier("oracleSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
