package com.zzx.zzxaiagent.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.zzx.zzxaiagent.mapper.pgvector",
        sqlSessionFactoryRef = "db2SqlSessionFactory"
)
public class DataSourceTwoConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.two")
    public DataSource db2DataSource() {
        return DataSourceBuilder.create().build();
    }

    /* 专门为 PgVectorStore 准备的 JdbcTemplate */
    @Bean
    public JdbcTemplate pgJdbcTemplate(@Qualifier("db2DataSource") DataSource db2DataSource) {
        return new JdbcTemplate(db2DataSource);
    }

    @Bean
    public SqlSessionFactory db2SqlSessionFactory(
            @Qualifier("db2DataSource") DataSource db2DataSource) throws Exception {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(db2DataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/*.xml")
        );
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager db2TransactionManager(
            @Qualifier("db2DataSource") DataSource db2DataSource) {
        return new DataSourceTransactionManager(db2DataSource);
    }

    @Bean
    public SqlSessionTemplate db2SqlSessionTemplate(
            @Qualifier("db2SqlSessionFactory") SqlSessionFactory db2SqlSessionFactory) {
        return new SqlSessionTemplate(db2SqlSessionFactory);
    }
}