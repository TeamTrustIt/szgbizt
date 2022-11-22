package hu.bme.szgbizt.secushop.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SecurityDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.security")
    public DataSourceProperties securityDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource securityDataSource() {
        return securityDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate securityJdbcTemplate(@Qualifier("securityDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
