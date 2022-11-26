package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffData;
import hu.bme.szgbizt.secushop.dto.DetailedUser;
import hu.bme.szgbizt.secushop.dto.PatchPasswordRequest;
import hu.bme.szgbizt.secushop.dto.PatchProfileRequest;
import hu.bme.szgbizt.secushop.exception.*;
import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;
import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.CommentRepository;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_JPG;
import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_RAW;

@Service
@Transactional
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ShopUserRepository shopUserRepository;
    private final CaffDataRepository caffDataRepository;
    private final CommentRepository commentRepository;

    /**
     * @param passwordEncoder    The password encoder.
     * @param userRepository     Repository for {@link UserEntity}.
     * @param shopUserRepository Repository for {@link ShopUserEntity}.
     * @param caffDataRepository Repository for {@link CaffDataEntity}.
     * @param commentRepository  Repository for {@link CommentEntity}.
     */
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ShopUserRepository shopUserRepository, CaffDataRepository caffDataRepository, CommentRepository commentRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.shopUserRepository = shopUserRepository;
        this.caffDataRepository = caffDataRepository;
        this.commentRepository = commentRepository;
    }

    public DetailedUser getUser(UUID callerUserId, UUID userId) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntity = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!callerUserEntity.equals(userEntity)) {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }

        var shopUserEntity = shopUserRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        var caffData = shopUserEntity.getCaffData().stream()
                .map(caffDataEntity -> new CaffData(
                        caffDataEntity.getId(),
                        caffDataEntity.getName(),
                        caffDataEntity.getDescription(),
                        caffDataEntity.getPrice(),
                        caffDataEntity.getShopUser().getUsername(),
                        "imageUrl",
                        caffDataEntity.getUploadDate())
                )
                .collect(Collectors.toList());

        return new DetailedUser(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRoles(),
                shopUserEntity.getBalance(),
                caffData
        );

    }

    public void deleteUser(UUID callerUserId, UUID userIdToDelete) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToDelete = userRepository.findById(userIdToDelete)
                .orElseThrow(UserNotFoundException::new);

        if (!callerUserEntity.equals(userEntityToDelete)) {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }

        shopUserRepository.deleteById(userIdToDelete);
        userRepository.deleteById(userIdToDelete);
    }

    public void deleteComment(UUID callerUserId, UUID commentIdToDelete) {

        var callerUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var commentEntityToDelete = commentRepository.findById(commentIdToDelete)
                .orElseThrow(CommentNotFoundException::new);

        var callerUserComments = callerUserEntity.getComments().stream().map(CommentEntity::getId).collect(Collectors.toList());
        if (callerUserComments.contains(commentIdToDelete)) {
            commentRepository.delete(commentEntityToDelete);
        } else {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }
    }

    public void deleteCaffData(UUID callerUserId, UUID caffDataIdToDelete) {

        var callerUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var caffDataEntity = caffDataRepository.findById(caffDataIdToDelete)
                .orElseThrow(CaffDataNotFoundException::new);

        var callerCaffDataList = callerUserEntity.getCaffData().stream().map(CaffDataEntity::getId).collect(Collectors.toList());
        if (callerCaffDataList.contains(caffDataIdToDelete)) {

            var filename = caffDataEntity.getName();
            try {
                var pathRaw = PATH_CAFF_DATA_RAW.resolve(filename);
                var pathJpg = PATH_CAFF_DATA_JPG.resolve(filename);

                caffDataRepository.delete(caffDataEntity);
                Files.delete(pathRaw);
                Files.delete(pathJpg);

            } catch (IOException e) {
                LOGGER.error("Error while deleting caff data [{}]", filename);
                throw new SecuShopInternalServerException();
            }
        } else {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }
    }

    public void modifyPassword(UUID callerUserId, UUID userIdToModify, PatchPasswordRequest patchPasswordRequest) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToModify = userRepository.findById(userIdToModify)
                .orElseThrow(UserNotFoundException::new);

        if (!callerUserEntity.equals(userEntityToModify)) {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }

        var currentPassword = patchPasswordRequest.getCurrentPassword();
        var newPassword = patchPasswordRequest.getNewPassword();

        if (!passwordEncoder.matches(currentPassword, callerUserEntity.getPassword())) {
            LOGGER.error(ErrorCode.SS_0104.getMessage());
            throw new PasswordMismatchException();
        }

        userEntityToModify.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntityToModify);
    }

    public void modifyProfile(UUID callerUserId, UUID userIdToModify, PatchProfileRequest patchProfileRequest) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToModify = userRepository.findById(userIdToModify)
                .orElseThrow(UserNotFoundException::new);

        if (!callerUserEntity.equals(userEntityToModify)) {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }

        var newUsername = patchProfileRequest.getUsername();
        if (!callerUserEntity.getUsername().equals(newUsername)) {

            validateUserName(newUsername);
            userEntityToModify.setUsername(newUsername);

            var shopUserEntity = shopUserRepository.findById(callerUserId)
                    .orElseThrow(UserNotFoundException::new);

            shopUserEntity.setUsername(newUsername);
            shopUserRepository.save(shopUserEntity);
        }

        var newEmail = patchProfileRequest.getEmail();
        if (!callerUserEntity.getEmail().equals(newEmail)) {

            validateEmail(newEmail);
            userEntityToModify.setEmail(newEmail);
        }

        userRepository.save(userEntityToModify);
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
