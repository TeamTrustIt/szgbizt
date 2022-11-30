package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffData;
import hu.bme.szgbizt.secushop.dto.DetailedUser;
import hu.bme.szgbizt.secushop.exception.CaffDataNotFoundException;
import hu.bme.szgbizt.secushop.exception.CommentNotFoundException;
import hu.bme.szgbizt.secushop.exception.NoAuthorityToProcessException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.util.handler.FileHandler.deleteCaffDataJpg;
import static hu.bme.szgbizt.secushop.util.handler.FileHandler.deleteCaffDataRaw;

@Service
@Transactional
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final DateTimeFormatter dateTimeFormatter;
    private final UserRepository userRepository;
    private final ShopUserRepository shopUserRepository;
    private final CaffDataRepository caffDataRepository;
    private final CommentRepository commentRepository;

    /**
     * @param dateTimeFormatter  The formatter of {@link LocalDateTime}.
     * @param userRepository     Repository for {@link UserEntity}.
     * @param shopUserRepository Repository for {@link ShopUserEntity}.
     * @param caffDataRepository Repository for {@link CaffDataEntity}.
     * @param commentRepository  Repository for {@link CommentEntity}.
     */
    @Autowired
    public UserService(DateTimeFormatter dateTimeFormatter, UserRepository userRepository, ShopUserRepository shopUserRepository, CaffDataRepository caffDataRepository, CommentRepository commentRepository) {
        this.dateTimeFormatter = dateTimeFormatter;
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
            throw new NoAuthorityToProcessException();
        }

        var shopUserEntity = shopUserRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        var caffData = shopUserEntity.getUploadedCaffData().stream()
                .map(caffDataEntity -> new CaffData(
                        caffDataEntity.getId(),
                        caffDataEntity.getName(),
                        caffDataEntity.getDescription(),
                        caffDataEntity.getPrice(),
                        caffDataEntity.getShopUser().getUsername(),
                        caffDataEntity.getImageUrl(),
                        caffDataEntity.getUploadDate().format(dateTimeFormatter))
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
            throw new NoAuthorityToProcessException();
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
            throw new NoAuthorityToProcessException();
        }
    }

    public void deleteCaffData(UUID callerUserId, UUID caffDataIdToDelete) {

        var callerUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var caffDataEntity = caffDataRepository.findById(caffDataIdToDelete)
                .orElseThrow(CaffDataNotFoundException::new);

        var callerCaffDataList = callerUserEntity.getUploadedCaffData().stream().map(CaffDataEntity::getId).collect(Collectors.toList());
        if (callerCaffDataList.contains(caffDataIdToDelete)) {

            var filename = caffDataEntity.getName();
            deleteCaffDataRaw(filename);
            deleteCaffDataJpg(filename);
            caffDataRepository.delete(caffDataEntity);

        } else {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException();
        }
    }
}
