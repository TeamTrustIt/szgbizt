package hu.bme.szgbizt.secushop.security.filter;

import hu.bme.szgbizt.secushop.security.persistence.repository.BlackListedAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.Constant.SYSTEM_BASE_URL;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtBlackListFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<String> ROLE_FREE_PATHS = List.of(
            "/login",
            "/registration"
    );

    private final BlackListedAccessTokenRepository blackListedAccessTokenRepository;

    @Autowired
    public JwtBlackListFilter(BlackListedAccessTokenRepository blackListedAccessTokenRepository) {
        this.blackListedAccessTokenRepository = blackListedAccessTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var path = request.getServletPath();
        var authorizationHeader = request.getHeader(AUTHORIZATION);

        if (!isRoleFreePath(path) && isBearerToken(authorizationHeader)) {

            var accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
            if (isBlackListedAccessToken(accessToken)) {
                response.setStatus(UNAUTHORIZED.value());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isBlackListedAccessToken(String token) {
        return blackListedAccessTokenRepository.findByToken(token).isPresent();
    }

    private boolean isBearerToken(String authorizationHeader) {
        return nonNull(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX);
    }

    private boolean isRoleFreePath(String path) {
        return buildRoleFreeFullPaths().contains(path);
    }

    private List<String> buildRoleFreeFullPaths() {
        return ROLE_FREE_PATHS.stream().map(path -> SYSTEM_BASE_URL + path).collect(Collectors.toList());
    }
}
