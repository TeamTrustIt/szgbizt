package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.LoggedUser;
import hu.bme.szgbizt.secushop.dto.PostRegistrationRequest;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.security.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private SecurityController securityController;
    private Authentication authentication;

    @Mock
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        securityController = new SecurityController(securityService);
        authentication = new JwtAuthenticationToken(mock(Jwt.class), List.of(mock(SimpleGrantedAuthority.class)), UUID_DEFAULT.toString());
    }

    @Test
    void testLogin() {

        // Arrange
        when(securityService.login(any(Authentication.class))).thenReturn(mock(LoggedUser.class));

        // Act
        var loggedUser = securityController.login(authentication);

        // Assert
        assertNotNull(loggedUser);
    }

    @Test
    void testRegistration() {

        // Arrange
        var postRegistrationRequest = mock(PostRegistrationRequest.class);
        when(securityService.registration(any(PostRegistrationRequest.class))).thenReturn(mock(RegisteredUser.class));

        // Act
        var registeredUser = securityController.registration(postRegistrationRequest);

        // Assert
        assertNotNull(registeredUser);
    }

    @Test
    void testLogout() {

        try {

            // Arrange
            doNothing().when(securityService).logout(any(Authentication.class));

            // Act
            securityController.logout(authentication);

        } catch (Exception ex) {
            fail();
        }
    }
}
