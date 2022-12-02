package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.DetailedUser;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.service.AdminService;
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
class AdminControllerTest {

    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private AdminController adminController;
    private Authentication authentication;

    @Mock
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminController = new AdminController(adminService);
        authentication = new JwtAuthenticationToken(mock(Jwt.class), List.of(mock(SimpleGrantedAuthority.class)), UUID_DEFAULT.toString());
    }

    @Test
    void testGetUsersByAdmin() {

        // Arrange
        when(adminService.getUsers()).thenReturn(List.of(mock(RegisteredUser.class)));

        // Act
        var registeredUsers = adminController.getUsers(authentication);

        // Assert
        assertNotNull(registeredUsers);
    }

    @Test
    void testGetUserByAdmin() {

        // Arrange
        when(adminService.getUser(any(UUID.class))).thenReturn(mock(DetailedUser.class));

        // Act
        var detailedUser = adminController.getUser(authentication, UUID_DEFAULT);

        // Assert
        assertNotNull(detailedUser);
    }

    @Test
    void testDeleteUserByAdmin() {

        try {

            // Arrange
            doNothing().when(adminService).deleteUser(UUID_DEFAULT, UUID_DEFAULT);

            // Act
            adminController.deleteUser(authentication, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCaffDataByAdmin() {

        try {

        // Arrange
        doNothing().when(adminService).deleteCaffData(UUID_DEFAULT);

        // Act
        adminController.deleteCaffData(authentication, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCommentByAdmin() {

        try {

            // Arrange
            doNothing().when(adminService).deleteComment(UUID_DEFAULT);

            // Act
            adminController.deleteComment(authentication, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }
}
