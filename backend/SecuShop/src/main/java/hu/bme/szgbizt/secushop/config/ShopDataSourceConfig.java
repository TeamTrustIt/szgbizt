package hu.bme.szgbizt.secushop.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ShopDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.shop")
    public DataSourceProperties shopDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource shopDataSource() {
        return shopDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate shopJdbcTemplate(@Qualifier("shopDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
