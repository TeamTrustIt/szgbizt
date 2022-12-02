package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.exception.CaffDataNotFoundException;
import hu.bme.szgbizt.secushop.exception.CommentNotFoundException;
import hu.bme.szgbizt.secushop.exception.SelfDeletionException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.CommentRepository;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import hu.bme.szgbizt.secushop.util.handler.FileHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class AdminServiceTest {

    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private AdminService adminService;

    @Mock
    private DateTimeFormatter dateTimeFormatter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShopUserRepository shopUserRepository;

    @Mock
    private CaffDataRepository caffDataRepository;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(dateTimeFormatter, userRepository, shopUserRepository, caffDataRepository, commentRepository);
    }

    @Test
    void testGetUsers() {

        // Arrange
        when(userRepository.findByRolesIs(anyString())).thenReturn(List.of(mock(UserEntity.class)));

        // Act
        var registeredUsers = adminService.getUsers();

        // Assert
        assertNotNull(registeredUsers);
    }

    @Test
    void testGetUserButUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> adminService.getUser(UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testGetUserButUserNotFoundInShopUserRepository() {

        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(mock(UserEntity.class)));
        when(shopUserRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> adminService.getUser(UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testGetUserHappyPath() {

        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(mock(UserEntity.class)));
        when(shopUserRepository.findById(any(UUID.class))).thenReturn(Optional.of(mock(ShopUserEntity.class)));

        // Act
        var detailedUser = adminService.getUser(UUID_DEFAULT);

        // Assert
        assertNotNull(detailedUser);
    }

    @Test
    void testDeleteUserButUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> adminService.deleteUser(UUID_DEFAULT, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteUserButUserIsHimOrHerself() {

        // Arrange
        var selfUserEntity = mock(UserEntity.class);
        when(selfUserEntity.getId()).thenReturn(UUID_DEFAULT);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(selfUserEntity));

        // Act
        var selfDeletionException = assertThrows(SelfDeletionException.class,
                () -> adminService.deleteUser(UUID_DEFAULT, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0151, selfDeletionException.getErrorCode());
    }

    @Test
    void testDeleteUserHappyPath() {

        try {

            // Arrange
            var selfUserEntity = mock(UserEntity.class);
            when(selfUserEntity.getId()).thenReturn(UUID.randomUUID());
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(selfUserEntity));
            doNothing().when(shopUserRepository).deleteById(any());
            doNothing().when(userRepository).deleteById(any());

            // Act
            adminService.deleteUser(UUID_DEFAULT, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCaffDataButCaffDataNotFound() {

        // Arrange
        when(caffDataRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        var caffDataNotFoundException = assertThrows(CaffDataNotFoundException.class,
                () -> adminService.deleteCaffData(UUID_DEFAULT));

        // Assert
        assertEquals(SS_0142, caffDataNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteCaffDataHappyPath() {

        try (var ignored = mockStatic(FileHandler.class)) {

            // Arrange
            when(caffDataRepository.findById(any(UUID.class))).thenReturn(Optional.of(mock(CaffDataEntity.class)));
            doNothing().when(caffDataRepository).delete(any(CaffDataEntity.class));

            // Act
            adminService.deleteCaffData(UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCommentButCommentNotFound() {

        // Arrange
        when(commentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        var commentNotFoundException = assertThrows(CommentNotFoundException.class,
                () -> adminService.deleteComment(UUID_DEFAULT));

        // Assert
        assertEquals(SS_0143, commentNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteCommentHappyPath() {

        try {

            // Arrange
            when(commentRepository.findById(any(UUID.class))).thenReturn(Optional.of(mock(CommentEntity.class)));
            doNothing().when(commentRepository).delete(any(CommentEntity.class));

            // Act
            adminService.deleteComment(UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }
}
