package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.BaseCaffData;
import hu.bme.szgbizt.secushop.dto.PutRegisteredUserRequest;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.dto.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ShopUserRepository shopUserRepository, CaffDataRepository caffDataRepository, CommentRepository commentRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.shopUserRepository = shopUserRepository;
        this.caffDataRepository = caffDataRepository;
        this.commentRepository = commentRepository;
    }

    public User getUser(UUID callerUserId, UUID userId) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntity = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (callerUserEntity.equals(userEntity)) {
            var shopUserEntity = shopUserRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);

            var caffData = shopUserEntity.getCaffData().stream()
                    .map(caffDataEntity -> new BaseCaffData(
                            caffDataEntity.getId(),
                            caffDataEntity.getName(),
                            caffDataEntity.getDescription()
                    ))
                    .collect(Collectors.toList());

            return new User(
                    userEntity.getId(),
                    userEntity.getUsername(),
                    userEntity.getEmail(),
                    userEntity.getRoles(),
                    shopUserEntity.getBalance(),
                    caffData
            );
        }

        LOGGER.error(ErrorCode.SS_0152.getMessage());
        throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
    }

    public RegisteredUser updateUser(UUID callerUserId, UUID userIdToUpdate, PutRegisteredUserRequest putRegisteredUserRequest) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToUpdate = userRepository.findById(userIdToUpdate)
                .orElseThrow(UserNotFoundException::new);

        if (callerUserEntity.equals(userEntityToUpdate)) {

            var username = putRegisteredUserRequest.getUsername();
            validateUserName(username);

            var email = putRegisteredUserRequest.getEmail();
            validateEmail(email);

            userEntityToUpdate.setUsername(username);
            userEntityToUpdate.setPassword(passwordEncoder.encode(putRegisteredUserRequest.getPassword()));
            userEntityToUpdate.setEmail(email);

            var updatedUserEntity = userRepository.save(userEntityToUpdate);

            return new RegisteredUser(
                    updatedUserEntity.getId(),
                    updatedUserEntity.getUsername(),
                    updatedUserEntity.getEmail(),
                    updatedUserEntity.getRoles()
            );
        }

        LOGGER.error(ErrorCode.SS_0152.getMessage());
        throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
    }

    @Transactional
    public void deleteUser(UUID callerUserId, UUID userIdToDelete) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToDelete = userRepository.findById(userIdToDelete)
                .orElseThrow(UserNotFoundException::new);

        if (callerUserEntity.equals(userEntityToDelete)) {

            shopUserRepository.deleteById(userIdToDelete);
            userRepository.deleteById(userIdToDelete);
        } else {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }
    }

    public void deleteCaffData(UUID callerUserId, UUID caffDataIdToDelete) {

        var callerUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var caffDataEntityToDelete = caffDataRepository.findById(caffDataIdToDelete)
                .orElseThrow(CaffDataNotFoundException::new);

        if (callerUserEntity.getCaffData().contains(caffDataEntityToDelete)) {
            caffDataRepository.delete(caffDataEntityToDelete);
        } else {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }
    }

    public void deleteComment(UUID callerUserId, UUID commentIdToDelete) {

        var callerUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var commentEntityToDelete = commentRepository.findById(commentIdToDelete)
                .orElseThrow(CommentNotFoundException::new);

        if (callerUserEntity.getComments().contains(commentEntityToDelete)) {
            commentRepository.delete(commentEntityToDelete);
        } else {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
        }
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
