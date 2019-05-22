package com.learncamel.learncamelspringboot.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    Logger logger = LoggerFactory.getLogger(DbConfig.class);

    @Bean(name="dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        logger.info("---------Method DataSource dataSource---- ");
        DataSource ds = DataSourceBuilder.create().build();
        return ds;
    }
}
