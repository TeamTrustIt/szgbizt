package hu.bme.szgbizt.secushop.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class JwtHandler {

    private JwtHandler() {
        // Empty constructor.
    }

    public static UUID getUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    public static String getTokenValue(Authentication authentication) {
        var jwt = (Jwt) authentication.getPrincipal();
        return jwt.getTokenValue();
    }
}
