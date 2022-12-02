package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.DetailedUser;
import hu.bme.szgbizt.secushop.service.UserService;
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

@ExtendWith(value = MockitoExtension.class)
class UserControllerTest {

    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private UserController userController;
    private Authentication authentication;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
        authentication = new JwtAuthenticationToken(mock(Jwt.class), List.of(mock(SimpleGrantedAuthority.class)), UUID_DEFAULT.toString());
    }

    @Test
    void testGetUserByUser() {

        // Arrange
        when(userService.getUser(any(UUID.class), any(UUID.class))).thenReturn(mock(DetailedUser.class));

        // Act
        var detailedUser = userController.getUser(authentication, UUID_DEFAULT);

        // Assert
        assertNotNull(detailedUser);
    }

    @Test
    void testDeleteUserByUser() {

        try {

            // Arrange
            doNothing().when(userService).deleteUser(any(UUID.class), any(UUID.class));

            // Act
            userController.deleteUser(authentication, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCommentByUser() {

        try {

            // Arrange
            doNothing().when(userService).deleteComment(any(UUID.class), any(UUID.class));

            // Act
            userController.deleteComment(authentication, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCaffData() {

        try {

            // Arrange
            doNothing().when(userService).deleteCaffData(any(UUID.class), any(UUID.class));

            // Act
            userController.deleteCaffData(authentication, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }
}
