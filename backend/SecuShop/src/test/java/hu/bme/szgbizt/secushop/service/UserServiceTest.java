package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.exception.CaffDataNotFoundException;
import hu.bme.szgbizt.secushop.exception.CommentNotFoundException;
import hu.bme.szgbizt.secushop.exception.NoAuthorityToProcessException;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class UserServiceTest {

    private static final UUID UUID_CALLER = UUID.randomUUID();
    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private UserService userService;

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
        userService = new UserService(dateTimeFormatter, userRepository, shopUserRepository, caffDataRepository, commentRepository);
    }

    @Test
    void testGetUserButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testGetUserButUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testGetUserButUserNotEqualsTheSpecifiedUserToGet() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(UserEntity.class)));

        // Act
        var noAuthorityToProcessException = assertThrows(NoAuthorityToProcessException.class,
                () -> userService.getUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0152, noAuthorityToProcessException.getErrorCode());
    }

    @Test
    void testGetUserButNotFoundInShopUserRepository() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
        when(shopUserRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testGetUserHappyPath() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
        when(shopUserRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(ShopUserEntity.class)));

        // Act
        var detailedUser = userService.getUser(UUID_CALLER, UUID_DEFAULT);

        // Assert
        assertNotNull(detailedUser);
    }

    @Test
    void testDeleteUserButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteUserButUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteUserButUserNotEqualsTheSpecifiedUserToDelete() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(UserEntity.class)));

        // Act
        var noAuthorityToProcessException = assertThrows(NoAuthorityToProcessException.class,
                () -> userService.deleteUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0152, noAuthorityToProcessException.getErrorCode());
    }

    @Test
    void testDeleteUserButNotFoundInShopUserRepository() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
        when(shopUserRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteUserHappyPath() {

        try {

            // Arrange
            var userEntity = mock(UserEntity.class);
            when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
            when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
            when(shopUserRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
            doNothing().when(shopUserRepository).delete(any(ShopUserEntity.class));
            doNothing().when(userRepository).delete(any(UserEntity.class));

            // Act
            userService.deleteUser(UUID_CALLER, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCommentButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.deleteComment(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteCommentButCommentNotFound() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
        when(commentRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var commentNotFoundException = assertThrows(CommentNotFoundException.class,
                () -> userService.deleteComment(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0143, commentNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteCommentButCommentNotPartOfTheCallerUser() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
        when(commentRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(CommentEntity.class)));

        // Act
        var noAuthorityToProcessException = assertThrows(NoAuthorityToProcessException.class,
                () -> userService.deleteComment(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0152, noAuthorityToProcessException.getErrorCode());
    }

    @Test
    void testDeleteCommentHappyPath() {

        try {

            // Arrange
            var shopUser = mock(ShopUserEntity.class);
            var commentEntity = mock(CommentEntity.class);
            when(shopUser.getComments()).thenReturn(Set.of(commentEntity));
            when(commentEntity.getId()).thenReturn(UUID_DEFAULT);
            when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(shopUser));
            when(commentRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(commentEntity));
            doNothing().when(commentRepository).delete(any(CommentEntity.class));

            // Act
            userService.deleteComment(UUID_CALLER, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testDeleteCaffDataButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.deleteCaffData(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteCaffDataButCaffDataNotFound() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
        when(caffDataRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var caffDataNotFoundException = assertThrows(CaffDataNotFoundException.class,
                () -> userService.deleteCaffData(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0142, caffDataNotFoundException.getErrorCode());
    }

    @Test
    void testDeleteCaffDataButCaffDataNotPartOfTheCallerUser() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
        when(caffDataRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(CaffDataEntity.class)));

        // Act
        var noAuthorityToProcessException = assertThrows(NoAuthorityToProcessException.class,
                () -> userService.deleteCaffData(UUID_CALLER, UUID_DEFAULT));

        // Assert
        assertEquals(SS_0152, noAuthorityToProcessException.getErrorCode());
    }

    @Test
    void testDeleteCaffDataHappyPath() {

        try (var ignored = mockStatic(FileHandler.class)) {

            // Arrange
            var shopUser = mock(ShopUserEntity.class);
            var caffDataEntity = mock(CaffDataEntity.class);
            when(shopUser.getUploadedCaffData()).thenReturn(Set.of(caffDataEntity));
            when(caffDataEntity.getId()).thenReturn(UUID_DEFAULT);
            when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(shopUser));
            when(caffDataRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(caffDataEntity));
            doNothing().when(caffDataRepository).delete(any(CaffDataEntity.class));

            // Act
            userService.deleteCaffData(UUID_CALLER, UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }
}
