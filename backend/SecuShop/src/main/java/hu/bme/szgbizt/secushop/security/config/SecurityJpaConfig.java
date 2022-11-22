package hu.bme.szgbizt.secushop.security.config;

import hu.bme.szgbizt.secushop.security.persistence.entity.BlackListedAccessTokenEntity;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.BlackListedAccessTokenRepository;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = {UserRepository.class, BlackListedAccessTokenRepository.class},
        entityManagerFactoryRef = "securityEntityManagerFactory",
        transactionManagerRef = "securityTransactionManager"
)
public class SecurityJpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory(
            @Qualifier("securityDataSource") DataSource securityDataSource,
            EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(securityDataSource)
                .packages(UserEntity.class, BlackListedAccessTokenEntity.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager securityTransactionManager(
            @Qualifier("securityEntityManagerFactory") LocalContainerEntityManagerFactoryBean securityEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(securityEntityManagerFactory.getObject()));
    }
}
