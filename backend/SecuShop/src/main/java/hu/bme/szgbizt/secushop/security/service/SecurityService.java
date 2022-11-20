package hu.bme.szgbizt.secushop.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.Constant.CLAIM_ROLES;
import static hu.bme.szgbizt.secushop.util.Constant.SYSTEM_ID;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Service class for the security of application.
 */
@Service
public class SecurityService {

    private static final Integer EXPIRE_IN_MINUTES = 10;
    private final JwtEncoder jwtEncoder;

    /**
     * Instantiates a new {@link SecurityService}.
     *
     * @param jwtEncoder The encoder of JWT.
     */
    @Autowired
    public SecurityService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {

        var roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var now = now();
        var claims = JwtClaimsSet.builder()
                .issuer(SYSTEM_ID)
                .issuedAt(now)
                .expiresAt(now.plus(EXPIRE_IN_MINUTES, MINUTES))
                .subject(authentication.getName())
                .claim(CLAIM_ROLES, roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
