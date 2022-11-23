package hu.bme.szgbizt.secushop.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static hu.bme.szgbizt.secushop.util.Constant.CLAIM_USERNAME;

public final class JwtHandler {

    private JwtHandler() {
        // Empty constructor.
    }

    public static UUID getUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    public static String getUsername(Authentication authentication) {
        var jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaims().get(CLAIM_USERNAME).toString();
    }

    public static String getTokenValue(Authentication authentication) {
        var jwt = (Jwt) authentication.getPrincipal();
        return jwt.getTokenValue();
    }
}
