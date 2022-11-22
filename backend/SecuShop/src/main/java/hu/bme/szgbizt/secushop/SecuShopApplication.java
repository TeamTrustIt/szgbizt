package hu.bme.szgbizt.secushop;

import hu.bme.szgbizt.secushop.security.config.RsaKeyProperties;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static hu.bme.szgbizt.secushop.util.Constant.ROLE_ADMIN;
import static hu.bme.szgbizt.secushop.util.Constant.ROLE_USER;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SecuShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            var defaultPassword = passwordEncoder.encode("Pass1234");
            var admin = new UserEntity("admin", defaultPassword, "admin@admin.hu", ROLE_ADMIN);
            var user = new UserEntity("user", defaultPassword, "user@user.hu", ROLE_USER);

            //       userRepository.save(admin);
            //       userRepository.save(user);
        };
    }
}
