package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.exception.SelfDeletionException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.Constant.ROLE_USER;

@Service
public class AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final ShopUserRepository shopUserRepository;

    /**
     * Instantiates a new {@link AdminService}.
     *
     * @param userRepository     The repository for {@link UserEntity}.
     * @param shopUserRepository The repository for {@link ShopUserRepository}.
     */
    public AdminService(UserRepository userRepository, ShopUserRepository shopUserRepository) {
        this.userRepository = userRepository;
        this.shopUserRepository = shopUserRepository;
    }

    public List<RegisteredUser> getUsers() {
        return userRepository.findByRolesIs(ROLE_USER).stream()
                .map(userEntity -> new RegisteredUser(
                        userEntity.getId(),
                        userEntity.getUsername(),
                        userEntity.getEmail(),
                        userEntity.getRoles()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Authentication authentication, UUID userIdToDelete) {

        var selfUserEntity = userRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if (selfUserEntity.getId().equals(userIdToDelete)) {
            LOGGER.error("Cannot delete yourself");
            throw new SelfDeletionException();
        }

        shopUserRepository.deleteById(userIdToDelete);
        userRepository.deleteById(userIdToDelete);
    }
}
