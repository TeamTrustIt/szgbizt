package hu.bme.szgbizt.secushop.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA configuration properties.
 */
@ConfigurationProperties(prefix = "rsa")
public final class RsaKeyProperties {

    @Value("${rsa.private-key}")
    public RSAPrivateKey rsaPrivateKey;

    @Value("${rsa.public-key}")
    public RSAPublicKey rsaPublicKey;
}
