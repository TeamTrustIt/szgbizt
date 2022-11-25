package hu.bme.szgbizt.secushop.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import hu.bme.szgbizt.secushop.security.filter.JwtBlackListFilter;
import hu.bme.szgbizt.secushop.security.service.JpaUserDetailsService;
import hu.bme.szgbizt.secushop.security.util.JwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static hu.bme.szgbizt.secushop.config.SwaggerConfig.PUBLIC_SWAGGER_ENDPOINT_PATTERNS;
import static hu.bme.szgbizt.secushop.util.Constant.SYSTEM_BASE_URL;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configuration class for application security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINT_PATTERNS = new String[]{
            SYSTEM_BASE_URL + "/login",
            SYSTEM_BASE_URL + "/registration"
    };

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:4200"
    );

    private static final List<String> ALLOWED_HEADERS = List.of(
            "Origin", "Access-Control-Allow-Origin", "Content-Type",
            "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
            "Access-Control-Request-Method", "Access-Control-Request-Headers"
    );

    private static final List<String> EXPOSED_HEADERS = List.of(
            "Origin", "Content-Type", "Accept", "Authorization",
            "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
    );

    private static final List<String> ALLOWED_METHODS = List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
    );

    private final RsaKeyProperties rsaKeyProperties;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final JwtBlackListFilter jwtBlackListFilter;


    /**
     * Instantiates a new {@link SecurityConfig}.
     *
     * @param rsaKeyProperties      The RSA related private and public keys.
     * @param jpaUserDetailsService The {@link JpaUserDetailsService}.
     * @param jwtBlackListFilter    The filter for black listed tokens.
     */
    public SecurityConfig(RsaKeyProperties rsaKeyProperties, JpaUserDetailsService jpaUserDetailsService, JwtBlackListFilter jwtBlackListFilter) {
        this.rsaKeyProperties = rsaKeyProperties;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.jwtBlackListFilter = jwtBlackListFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(PUBLIC_SWAGGER_ENDPOINT_PATTERNS).permitAll()
                .mvcMatchers(PUBLIC_ENDPOINT_PATTERNS).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .userDetailsService(jpaUserDetailsService)
                .httpBasic(withDefaults())
                .addFilterBefore(jwtBlackListFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(ALLOWED_ORIGINS);
        corsConfiguration.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfiguration.setExposedHeaders(EXPOSED_HEADERS);
        corsConfiguration.setAllowedMethods(ALLOWED_METHODS);

        var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.rsaPublicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {

        var rsaKey = new RSAKey.Builder(rsaKeyProperties.rsaPublicKey).privateKey(rsaKeyProperties.rsaPrivateKey).build();
        var jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());

        return jwtAuthenticationConverter;
    }
}
