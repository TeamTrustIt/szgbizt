package hu.bme.szgbizt.secushop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private static final String RESOURCE_PACKAGE_OF_SHOP_CONTROLLERS = "hu.bme.szgbizt.secushop.controller";

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(RESOURCE_PACKAGE_OF_SHOP_CONTROLLERS))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
