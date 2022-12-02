package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.PatchPasswordRequest;
import hu.bme.szgbizt.secushop.dto.PatchProfileRequest;
import hu.bme.szgbizt.secushop.exception.*;
import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.CommentRepository;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
class SecuShopServiceTest {

    private static final UUID UUID_CALLER = UUID.randomUUID();
    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private SecuShopService secuShopService;

    @Mock
    private DateTimeFormatter dateTimeFormatter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CaffDataRepository caffDataRepository;

    @Mock
    private ShopUserRepository shopUserRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        secuShopService = new SecuShopService(dateTimeFormatter, passwordEncoder, caffDataRepository, shopUserRepository, commentRepository, userRepository);
    }

    @Test
    void testGetCaffDataList() {

        // Arrange
        when(caffDataRepository.findAll()).thenReturn(List.of());

        // Act
        var caffDataList = secuShopService.getCaffDataList();

        // Assert
        assertNotNull(caffDataList);
    }

    @Test
    void testGetCaffDataButNotFound() {

        // Arrange
        when(caffDataRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        var caffDataNotFoundException = assertThrows(CaffDataNotFoundException.class,
                () -> secuShopService.getCaffData(UUID_DEFAULT));

        // Assert
        assertEquals(SS_0142, caffDataNotFoundException.getErrorCode());
    }

    @Test
    void testGetCaffDataHappyPath() {

        try {

            // Arrange
            var caffDataEntity = mock(CaffDataEntity.class);
            when(caffDataRepository.findById(any(UUID.class))).thenReturn(Optional.of(caffDataEntity));
            when(caffDataEntity.getShopUser()).thenReturn(mock(ShopUserEntity.class));
            when(caffDataEntity.getUploadDate()).thenReturn(mock(LocalDateTime.class));

            // Act
            secuShopService.getCaffData(UUID_DEFAULT);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testPostCommentButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> secuShopService.postComment(UUID_CALLER, UUID_DEFAULT, "message"));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testPostCommentButCaffDataNotFound() {

        // Arrange
        when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
        when(caffDataRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        // Act
        var caffDataNotFoundException = assertThrows(CaffDataNotFoundException.class,
                () -> secuShopService.postComment(UUID_CALLER, UUID_DEFAULT, "message"));

        // Assert
        assertEquals(SS_0142, caffDataNotFoundException.getErrorCode());
    }

    @Test
    void testPostCommentHappyPath() {

        try {

            // Arrange
            var shopUserEntity = mock(ShopUserEntity.class);
            var caffDataEntity = mock(CaffDataEntity.class);
            when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(shopUserEntity));
            when(caffDataRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(caffDataEntity));
            var commentEntity = mock(CommentEntity.class);
            when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
            when(commentEntity.getShopUser()).thenReturn(shopUserEntity);
            when(commentEntity.getCaffData()).thenReturn(caffDataEntity);
            when(commentEntity.getUploadDate()).thenReturn(mock(LocalDateTime.class));

            // Act
            secuShopService.postComment(UUID_CALLER, UUID_DEFAULT, "message");

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testModifyPasswordButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());
        var patchPasswordRequest = mock(PatchPasswordRequest.class);

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> secuShopService.modifyPassword(UUID_CALLER, UUID_DEFAULT, patchPasswordRequest));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testModifyPasswordButUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());
        var patchPasswordRequest = mock(PatchPasswordRequest.class);

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> secuShopService.modifyPassword(UUID_CALLER, UUID_DEFAULT, patchPasswordRequest));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testModifyPasswordButUserNotEqualsTheSpecifiedUserToModify() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(UserEntity.class)));
        var patchPasswordRequest = mock(PatchPasswordRequest.class);

        // Act
        var noAuthorityToProcessException = assertThrows(NoAuthorityToProcessException.class,
                () -> secuShopService.modifyPassword(UUID_CALLER, UUID_DEFAULT, patchPasswordRequest));

        // Assert
        assertEquals(SS_0152, noAuthorityToProcessException.getErrorCode());
    }

    @Test
    void testModifyPasswordButPasswordMismatch() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userEntity.getPassword()).thenReturn("currentPassword");
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
        var patchPasswordRequest = mock(PatchPasswordRequest.class);
        when(patchPasswordRequest.getCurrentPassword()).thenReturn("invalidCurrentPassword");

        // Act
        var passwordMismatchException = assertThrows(PasswordMismatchException.class,
                () -> secuShopService.modifyPassword(UUID_CALLER, UUID_DEFAULT, patchPasswordRequest));

        // Assert
        assertEquals(SS_0104, passwordMismatchException.getErrorCode());
    }

    @Test
    void testModifyPasswordHappyPath() {

        try {

            // Arrange
            var userEntity = mock(UserEntity.class);
            when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
            when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
            var patchPasswordRequest = mock(PatchPasswordRequest.class);
            when(passwordEncoder.matches(any(), any())).thenReturn(true);
            when(userRepository.save(any(UserEntity.class))).thenReturn(mock(UserEntity.class));

            // Act
            secuShopService.modifyPassword(UUID_CALLER, UUID_DEFAULT, patchPasswordRequest);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testModifyProfileButCallerUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.empty());
        var patchProfileRequest = mock(PatchProfileRequest.class);

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> secuShopService.modifyProfile(UUID_CALLER, UUID_DEFAULT, patchProfileRequest));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testModifyProfileButUserNotFoundInUserRepository() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());
        var patchProfileRequest = mock(PatchProfileRequest.class);

        // Act
        var userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> secuShopService.modifyProfile(UUID_CALLER, UUID_DEFAULT, patchProfileRequest));

        // Assert
        assertEquals(SS_0141, userNotFoundException.getErrorCode());
    }

    @Test
    void testModifyProfileButUserNotEqualsTheSpecifiedUserToModify() {

        // Arrange
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(mock(UserEntity.class)));
        var patchProfileRequest = mock(PatchProfileRequest.class);

        // Act
        var noAuthorityToProcessException = assertThrows(NoAuthorityToProcessException.class,
                () -> secuShopService.modifyProfile(UUID_CALLER, UUID_DEFAULT, patchProfileRequest));

        // Assert
        assertEquals(SS_0152, noAuthorityToProcessException.getErrorCode());
    }

    @Test
    void testModifyProfileButUsernameIsInvalid() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userEntity.getUsername()).thenReturn("username");
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(mock(UserEntity.class)));
        var patchProfileRequest = new PatchProfileRequest("existingUsername", "email");

        // Act
        var usernameNotUniqueException = assertThrows(UsernameNotUniqueException.class,
                () -> secuShopService.modifyProfile(UUID_CALLER, UUID_DEFAULT, patchProfileRequest));

        // Assert
        assertEquals(SS_0120, usernameNotUniqueException.getErrorCode());
    }

    @Test
    void testModifyProfileButEmailIsInvalid() {

        // Arrange
        var userEntity = mock(UserEntity.class);
        when(userEntity.getUsername()).thenReturn("username");
        when(userEntity.getEmail()).thenReturn("email");
        when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail("existingEmail")).thenReturn(Optional.of(mock(UserEntity.class)));
        var patchProfileRequest = new PatchProfileRequest("username", "existingEmail");

        // Act
        var emailNotUniqueException = assertThrows(EmailNotUniqueException.class,
                () -> secuShopService.modifyProfile(UUID_CALLER, UUID_DEFAULT, patchProfileRequest));

        // Assert
        assertEquals(SS_0121, emailNotUniqueException.getErrorCode());
    }

    @Test
    void testModifyProfileHappyPath() {

        try {

            // Arrange
            var userEntity = mock(UserEntity.class);
            when(userEntity.getUsername()).thenReturn("username");
            when(userEntity.getEmail()).thenReturn("email");
            when(userRepository.findById(UUID_CALLER)).thenReturn(Optional.of(userEntity));
            when(userRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(userEntity));
            when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            when(shopUserRepository.findById(UUID_CALLER)).thenReturn(Optional.of(mock(ShopUserEntity.class)));
            when(shopUserRepository.save(any(ShopUserEntity.class))).thenReturn(mock(ShopUserEntity.class));
            when(userRepository.save(any(UserEntity.class))).thenReturn(mock(UserEntity.class));
            var patchProfileRequest = new PatchProfileRequest("newUsername", "newEmail");

            // Act
            secuShopService.modifyProfile(UUID_CALLER, UUID_DEFAULT, patchProfileRequest);

        } catch (Exception ex) {
            fail();
        }
    }
}
