package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffData;
import hu.bme.szgbizt.secushop.dto.DetailedUser;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.exception.CaffDataNotFoundException;
import hu.bme.szgbizt.secushop.exception.SecuShopInternalServerException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.Constant.ROLE_USER;

@Service
@Transactional
public class AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    public static final Path PATH_CAFF_DATA_RAW = Paths.get("caffdata/raw");
    public static final Path PATH_CAFF_DATA_JPG = Paths.get("caffdata/jpg");

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
    @Autowired
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
                        userEntity.getRoles())
                )
                .collect(Collectors.toList());
    }

    public DetailedUser getUser(UUID userId) {

        var userEntity = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

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

        var selfUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);

        if (selfUserEntity.getId().equals(userIdToDelete)) {
            LOGGER.error("Cannot delete yourself");
            throw new SelfDeletionException();
        }

        shopUserRepository.deleteById(userIdToDelete);
        userRepository.deleteById(userIdToDelete);
    }

    public void deleteCaffData(UUID caffDataIdToDelete) {

        var caffDataEntity = caffDataRepository.findById(caffDataIdToDelete)
                .orElseThrow(CaffDataNotFoundException::new);

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
    }

    public void deleteComment(UUID commentIdToDelete) {
        commentRepository.deleteById(commentIdToDelete);
    }
}
