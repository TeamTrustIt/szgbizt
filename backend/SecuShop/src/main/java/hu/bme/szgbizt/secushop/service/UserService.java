package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.BaseCaffData;
import hu.bme.szgbizt.secushop.dto.PutRegisteredUserRequest;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.exception.EmailNotUniqueException;
import hu.bme.szgbizt.secushop.exception.NoAuthorityToProcessException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.exception.UsernameNotUniqueException;
import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ShopUserRepository shopUserRepository;

    /**
     * @param passwordEncoder    The password encoder.
     * @param userRepository     Repository for {@link UserEntity}.
     * @param shopUserRepository Repository for {@link ShopUserEntity}.
     */
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ShopUserRepository shopUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.shopUserRepository = shopUserRepository;
    }

    public User getUser(UUID userId) {
        var userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var shopUserEntity = shopUserRepository.findById(userId).orElseThrow(UserNotFoundException::new);

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

    public RegisteredUser updateUser(UUID callerUserId, UUID userIdToUpdate, PutRegisteredUserRequest putRegisteredUserRequest) {

        var username = putRegisteredUserRequest.getUsername();
        validateUserName(username);

        var email = putRegisteredUserRequest.getEmail();
        validateEmail(email);

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToUpdate = userRepository.findById(userIdToUpdate)
                .orElseThrow(UserNotFoundException::new);

        if (callerUserEntity.equals(userEntityToUpdate)) {
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

        LOGGER.error("Cannot update other users");
        throw new NoAuthorityToProcessException(ErrorCode.SS_0152);
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
