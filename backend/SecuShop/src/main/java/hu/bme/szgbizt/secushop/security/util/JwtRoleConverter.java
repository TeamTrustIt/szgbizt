package hu.bme.szgbizt.secushop.security.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.Constant.CLAIM_ROLES;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        var roles = source.getClaimAsStringList(CLAIM_ROLES);

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
