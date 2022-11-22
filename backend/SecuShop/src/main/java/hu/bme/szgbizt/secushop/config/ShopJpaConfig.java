package hu.bme.szgbizt.secushop.config;

import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.CommentRepository;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
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
        basePackageClasses = {CaffDataRepository.class, CommentRepository.class, ShopUserRepository.class},
        entityManagerFactoryRef = "shopEntityManagerFactory",
        transactionManagerRef = "shopTransactionManager"
)
public class ShopJpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean shopEntityManagerFactory(
            @Qualifier("shopDataSource") DataSource shopDataSource,
            EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(shopDataSource)
                .packages(CaffDataEntity.class, CommentEntity.class, ShopUserEntity.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager shopTransactionManager(
            @Qualifier("shopEntityManagerFactory") LocalContainerEntityManagerFactoryBean shopEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(shopEntityManagerFactory.getObject()));
    }
}
