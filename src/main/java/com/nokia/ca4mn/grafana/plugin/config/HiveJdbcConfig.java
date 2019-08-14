package com.nokia.ca4mn.grafana.plugin.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class HiveJdbcConfig {

    private static final Logger logger = LogManager.getLogger(HiveJdbcConfig.class);

    @Autowired
    private Environment env;

	// use druid datasource (.yml file using druid)
    @Bean(name = "hiveJdbcDataSource")
	public DruidDataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("hive.url"));
        dataSource.setDriverClassName(env.getProperty("hive.driver-class-name"));
        dataSource.setUsername(env.getProperty("hive.user"));
        dataSource.setPassword(env.getProperty("hive.password"));
        logger.debug("Hive DataSource Inject Successfully...");
        return dataSource;
    }

    @Bean(name = "hiveJdbcTemplate")
	public JdbcTemplate hiveJdbcTemplate(@Qualifier("hiveJdbcDataSource") DruidDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

	// use apache datasource
	@Bean(name = "JdbcDataSource")
	public DataSource getDataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setUrl(env.getProperty("hive.url"));
		dataSource.setDriverClassName(env.getProperty("hive.driver-class-name"));
		dataSource.setUsername(env.getProperty("hive.user"));
		dataSource.setPassword(env.getProperty("hive.password"));
		logger.debug("Hive DataSource Inject Successfully...");
		return dataSource;
	}

	@Bean(name = "JdbcTemplate")
	public JdbcTemplate hiveJdbcTemplate(@Qualifier("JdbcDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
