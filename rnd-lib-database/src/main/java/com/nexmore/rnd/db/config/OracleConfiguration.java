package com.nexmore.rnd.db.config;

import com.nexmore.rnd.db.annotation.RndMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@MapperScan(basePackages = "com.nexmore.rnd.*.mapper.oracle", annotationClass = RndMapper.class, sqlSessionFactoryRef = "oracleSqlSessionFactory")
public class OracleConfiguration {

	@Bean
	@ConfigurationProperties("oracle.datasource")
	public DataSourceProperties oracleDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource oracleDataSource() {
		return oracleDataSourceProperties().initializeDataSourceBuilder().build();
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
