package com.nexmore.rnd.db.config;

import com.nexmore.rnd.db.annotation.RndMapper;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "com.nexmore.rnd.*.mapper.mariadb", annotationClass = RndMapper.class, sqlSessionFactoryRef = "mariadbSqlSessionFactory")
public class MariaDBConfiguration {
	@Bean
	@Primary
	@ConfigurationProperties("mariadb.datasource")
	public DataSourceProperties mariadbDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	public BasicDataSource mariadbDataSource() {
		return mariadbDataSourceProperties().initializeDataSourceBuilder().type(BasicDataSource.class).build();
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
