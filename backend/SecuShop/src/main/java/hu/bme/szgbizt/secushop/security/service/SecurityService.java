package hu.bme.szgbizt.secushop.security.service;

import hu.bme.szgbizt.secushop.dto.PostRegistrationRequest;
import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.exception.EmailNotUniqueException;
import hu.bme.szgbizt.secushop.exception.UsernameNotUniqueException;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.Constant.*;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Service class for the security of application.
 */
@Service
public class SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    private static final Integer EXPIRE_IN_MINUTES = 10;

    private final ModelMapper modelMapper;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Instantiates a new {@link SecurityService}.
     *
     * @param modelMapper     The mapper between classes.
     * @param jwtEncoder      The encoder of JWT.
     * @param passwordEncoder The password encoder.
     * @param userRepository  The repository for {@link UserEntity}.
     */
    @Autowired
    public SecurityService(ModelMapper modelMapper, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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

    public User registration(PostRegistrationRequest postRegistrationRequest) {

        var username = postRegistrationRequest.getUsername();
        var email = postRegistrationRequest.getEmail();

        validateUserName(username);
        validateEmail(email);

        var userEntityToSave = new UserEntity(
                username,
                passwordEncoder.encode(postRegistrationRequest.getPassword()),
                email,
                ROLE_USER
        );

        var savedUserEntity = userRepository.save(userEntityToSave);

        return modelMapper.map(savedUserEntity, User.class);
    }

    private void validateUserName(String username) {
        userRepository.findByUsername(username).ifPresent(userEntity -> {
            LOGGER.error("Username [{}] is already taken", username);
            throw new UsernameNotUniqueException();
        });
    }

    private void validateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(userEntity -> {
            LOGGER.error("Email [{}] is already taken", email);
            throw new EmailNotUniqueException();
        });
    }
}
