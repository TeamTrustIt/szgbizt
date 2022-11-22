package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.exception.SelfDeletionException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.CommentRepository;
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
    private final CaffDataRepository caffDataRepository;
    private final CommentRepository commentRepository;

    /**
     * Instantiates a new {@link AdminService}.
     *
     * @param userRepository     The repository for {@link UserEntity}.
     * @param shopUserRepository The repository for {@link ShopUserRepository}.
     * @param caffDataRepository The repository for {@link CaffDataEntity}.
     * @param commentRepository  The repository for {@link CommentEntity}.
     */
    public AdminService(UserRepository userRepository, ShopUserRepository shopUserRepository, CaffDataRepository caffDataRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.shopUserRepository = shopUserRepository;
        this.caffDataRepository = caffDataRepository;
        this.commentRepository = commentRepository;
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

    public void deleteCaffData(UUID caffDataIdToDelete) {
        caffDataRepository.deleteById(caffDataIdToDelete);
    }

    public void deleteComment(UUID commentIdToDelete) {
        commentRepository.deleteById(commentIdToDelete);
    }
}
