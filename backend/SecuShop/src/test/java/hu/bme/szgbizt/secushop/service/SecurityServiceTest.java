package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.PostRegistrationRequest;
import hu.bme.szgbizt.secushop.exception.EmailNotUniqueException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.exception.UsernameNotUniqueException;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.BlackListedAccessTokenEntity;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.BlackListedAccessTokenRepository;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import hu.bme.szgbizt.secushop.security.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
class SecurityServiceTest {

    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private SecurityService securityService;
    private Authentication authentication;

    @Spy
    private JwtEncoder jwtEncoder;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShopUserRepository shopUserRepository;

    @Mock
    private BlackListedAccessTokenRepository blackListedAccessTokenRepository;

    @BeforeEach
    void setUp() {
        securityService = new SecurityService(jwtEncoder, passwordEncoder, userRepository, shopUserRepository, blackListedAccessTokenRepository);
        authentication = new JwtAuthenticationToken(mock(Jwt.class), List.of(mock(SimpleGrantedAuthority.class)), UUID_DEFAULT.toString());
    }

    @Test
    void testLoginButUserNotFound() {

        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> securityService.login(authentication));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testLoginHappyPath() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(userEntity.getId()).thenReturn(UUID_DEFAULT);
        when(jwtEncoder.encode(any())).thenReturn(mock(Jwt.class));

        // Act
        var loggedUser = securityService.login(authentication);

        // Assert
        assertNotNull(loggedUser);
    }

    @Test
    void testRegistrationButUsernameIsInvalid() {

        // Arrange
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        var postRegistrationRequest = mock(PostRegistrationRequest.class);

        // Act
        var usernameNotUniqueException = assertThrows(UsernameNotUniqueException.class,
                () -> securityService.registration(postRegistrationRequest));

        // Assert
        assertEquals(SS_0120, usernameNotUniqueException.getErrorCode());
    }

    @Test
    void testRegistrationButEmailIsInvalid() {

        // Arrange
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        var postRegistrationRequest = mock(PostRegistrationRequest.class);

        // Act
        var emailNotUniqueException = assertThrows(EmailNotUniqueException.class,
                () -> securityService.registration(postRegistrationRequest));

        // Assert
        assertEquals(SS_0121, emailNotUniqueException.getErrorCode());
    }

    @Test
    void testRegistrationHappyPath() {

        // Arrange
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(mock(UserEntity.class));
        when(shopUserRepository.save(any(ShopUserEntity.class))).thenReturn(mock(ShopUserEntity.class));
        var postRegistrationRequest = mock(PostRegistrationRequest.class);

        // Act
        var registeredUser = securityService.registration(postRegistrationRequest);

        // Assert
        assertNotNull(registeredUser);
    }

    @Test
    void testLogout() {

        try {

            // Arrange
            when(blackListedAccessTokenRepository.save(any(BlackListedAccessTokenEntity.class))).thenReturn(mock(BlackListedAccessTokenEntity.class));

            // Act
            securityService.logout(authentication);

        } catch (Exception ex) {
            fail();
        }
    }
}
