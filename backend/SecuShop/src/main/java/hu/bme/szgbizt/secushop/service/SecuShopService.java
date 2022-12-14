package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.*;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_JPG;
import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_RAW;
import static hu.bme.szgbizt.secushop.util.CaffParser.runParseCommand;
import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_CAFF;
import static hu.bme.szgbizt.secushop.util.Constant.ROLE_ADMIN;
import static hu.bme.szgbizt.secushop.util.handler.FileHandler.*;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;

@Service
@Transactional
public class SecuShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuShopService.class);

    private final DateTimeFormatter dateTimeFormatter;
    private final PasswordEncoder passwordEncoder;
    private final CaffDataRepository caffDataRepository;
    private final ShopUserRepository shopUserRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * Instantiates a new {@link CaffDataRepository}.
     *
     * @param dateTimeFormatter  The formatter of {@link LocalDateTime}.
     * @param passwordEncoder    The password encoder.
     * @param caffDataRepository The repository for {@link CaffDataEntity}.
     * @param shopUserRepository The repository for {@link ShopUserEntity}.
     * @param commentRepository  The repository for {@link CommentEntity}.
     * @param userRepository     The repository for {@link UserEntity}.
     */
    @Autowired
    public SecuShopService(DateTimeFormatter dateTimeFormatter, PasswordEncoder passwordEncoder, CaffDataRepository caffDataRepository, ShopUserRepository shopUserRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.passwordEncoder = passwordEncoder;
        this.caffDataRepository = caffDataRepository;
        this.shopUserRepository = shopUserRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public UrlResource getImage(String filename) {
        var filePackageDirectory = filename.substring(0, filename.length() - "_ciff0".length());
        var path = Paths.get(PATH_CAFF_DATA_JPG + "/" + filePackageDirectory + "/" + filename + ".jpg");

        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException ex) {
            LOGGER.error("Something went wrong under the image [{}] downloading, error: {}", filename, ex);
            throw new SecuShopInternalServerException();
        }
    }

    public DetailedCaffData createCaffData(UUID callerUserId, String filename, String description, MultipartFile file, HttpServletRequest httpServletRequest) {
        try {

            caffDataRepository.findByName(filename).ifPresent(ignored -> {
                LOGGER.error("The specified filename [{}] already exist", filename);
                throw new CaffDataAlreadyExistException();
            });

            var shopUserEntity = shopUserRepository.findById(callerUserId)
                    .orElseThrow(UserNotFoundException::new);

            var caffDataEntityToSave = new CaffDataEntity(
                    filename,
                    description,
                    ZERO,
                    buildImageUrl(httpServletRequest, filename),
                    shopUserEntity
            );
            shopUserEntity.getUploadedCaffData().add(caffDataEntityToSave);

            var filenameToSave = filename + FILE_EXTENSION_CAFF;
            Files.copy(file.getInputStream(), PATH_CAFF_DATA_RAW.resolve(Objects.requireNonNull(filenameToSave)));
            parseCaffData(filename);
            var savedCaffDataEntity = caffDataRepository.save(caffDataEntityToSave);

            return new DetailedCaffData(
                    savedCaffDataEntity.getId(),
                    savedCaffDataEntity.getName(),
                    savedCaffDataEntity.getDescription(),
                    savedCaffDataEntity.getPrice(),
                    savedCaffDataEntity.getShopUser().getUsername(),
                    savedCaffDataEntity.getImageUrl(),
                    savedCaffDataEntity.getUploadDate().format(dateTimeFormatter),
                    emptyList()
            );

        } catch (Exception ex) {

            if (ex instanceof CaffDataAlreadyExistException) {
                throw new CaffDataAlreadyExistException();
            }

            LOGGER.error("Error while saving caff data [{}], error: {}", filename, ex.getMessage());
            throw new SecuShopInternalServerException();
        }
    }

    public List<CaffData> getCaffDataList() {
        return caffDataRepository.findAll().stream()
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
    }

    public DetailedCaffData getCaffData(UUID caffDataId) {
        return caffDataRepository.findById(caffDataId)
                .map(caffDataEntity -> {

                            var caffComments = caffDataEntity.getComments().stream()
                                    .map(commentEntity -> new CaffComment(
                                            commentEntity.getId(),
                                            commentEntity.getMessage(),
                                            commentEntity.getShopUser().getUsername(),
                                            commentEntity.getCaffData().getId(),
                                            commentEntity.getUploadDate().format(dateTimeFormatter))
                                    )
                                    .collect(Collectors.toList());

                            return new DetailedCaffData(
                                    caffDataEntity.getId(),
                                    caffDataEntity.getName(),
                                    caffDataEntity.getDescription(),
                                    caffDataEntity.getPrice(),
                                    caffDataEntity.getShopUser().getUsername(),
                                    caffDataEntity.getImageUrl(),
                                    caffDataEntity.getUploadDate().format(dateTimeFormatter),
                                    caffComments
                            );
                        }
                )
                .orElseThrow(CaffDataNotFoundException::new);
    }

    public Resource getCaffDataAsResource(UUID callerUserId, UUID caffDataId) {

        if (!isDownloadable(callerUserId, caffDataId)) {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException();
        }

        var caffDataEntity = caffDataRepository.findById(caffDataId)
                .orElseThrow(CaffDataNotFoundException::new);
        var filename = caffDataEntity.getName();

        try {
            var file = PATH_CAFF_DATA_RAW.resolve(filename + FILE_EXTENSION_CAFF);
            var resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                LOGGER.error("Error while loading caff data [{}] as resource", filename);
                throw new SecuShopInternalServerException();
            }
        } catch (MalformedURLException ex) {
            LOGGER.error("Error while loading caff data [{}] as resource, error: {}", filename, ex.getMessage());
            throw new SecuShopInternalServerException();
        }
    }

    public CaffComment postComment(UUID callerUserId, UUID caffDataId, String message) {

        var shopUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var caffDataEntity = caffDataRepository.findById(caffDataId)
                .orElseThrow(CaffDataNotFoundException::new);

        var commentEntity = new CommentEntity(
                message,
                shopUserEntity,
                caffDataEntity
        );

        shopUserEntity.getComments().add(commentEntity);
        caffDataEntity.getComments().add(commentEntity);

        var savedCommentEntity = commentRepository.save(commentEntity);

        return new CaffComment(
                savedCommentEntity.getId(),
                savedCommentEntity.getMessage(),
                savedCommentEntity.getShopUser().getUsername(),
                savedCommentEntity.getCaffData().getId(),
                savedCommentEntity.getUploadDate().format(dateTimeFormatter)
        );
    }

    public void modifyPassword(UUID callerUserId, UUID userIdToModify, PatchPasswordRequest patchPasswordRequest) {

        var callerUserEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntityToModify = userRepository.findById(userIdToModify)
                .orElseThrow(UserNotFoundException::new);

        if (!callerUserEntity.equals(userEntityToModify)) {
            LOGGER.error(ErrorCode.SS_0152.getMessage());
            throw new NoAuthorityToProcessException();
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
            throw new NoAuthorityToProcessException();
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

    private boolean isDownloadable(UUID callerUserId, UUID caffDataId) {

        var shopUserEntity = shopUserRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var userEntity = userRepository.findById(callerUserId)
                .orElseThrow(UserNotFoundException::new);
        var caffDataEntityToDownload = caffDataRepository.findById(caffDataId)
                .orElseThrow(CaffDataNotFoundException::new);

        if (isAdmin(userEntity)) {
            return true;
        }

        if (isOwn(shopUserEntity, caffDataId)) {
            return true;
        }

        var userBalance = shopUserEntity.getBalance();
        var caffDataPrice = caffDataEntityToDownload.getPrice();
        if (userBalance.compareTo(caffDataPrice) < 0) {
            LOGGER.info("User [{}] can not download caff data [{}], do not have enough money", callerUserId, caffDataId);
            return false;
        }

        var newBalance = userBalance.subtract(caffDataPrice);
        shopUserEntity.setBalance(newBalance);
        shopUserEntity.getPurchasedCaffData().add(caffDataEntityToDownload);
        caffDataEntityToDownload.getCustomerUsers().add(shopUserEntity);
        shopUserRepository.save(shopUserEntity);
        LOGGER.info("Caff data [{}] is successfully purchased for user [{}]", callerUserId, caffDataId);

        return true;
    }

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRoles().equals(ROLE_ADMIN);
    }

    private boolean isOwn(ShopUserEntity shopUserEntity, UUID caffDataIdToDownload) {

        var uploadedCaffDataIds = shopUserEntity.getUploadedCaffData().stream().map(CaffDataEntity::getId).collect(Collectors.toSet());
        var purchasedCaffDataIds = shopUserEntity.getPurchasedCaffData().stream().map(CaffDataEntity::getId).collect(Collectors.toSet());
        if (uploadedCaffDataIds.contains(caffDataIdToDownload) || purchasedCaffDataIds.contains(caffDataIdToDownload)) {
            LOGGER.info("User [{}] can download caff data [{}]", shopUserEntity.getId(), caffDataIdToDownload);
            return true;
        }

        return false;
    }

    private String buildImageUrl(HttpServletRequest request, String filename) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1/secu-shop/images/" + filename + "_ciff0";
    }

    private void parseCaffData(String filename) {

        try {
            LOGGER.info("Parsing caff file [{}]", filename);

            createSubdirectoryForCiffData(filename);
            runParseCommand(filename);

        } catch (IOException | InterruptedException | CaffDataParsingException ex) {

            LOGGER.error("Something went wrong under the caff data [{}] parsing, error: {}", filename, ex.getMessage());
            LOGGER.info("Try to rollback caff data [{}] automatically", filename);

            deleteCaffDataRaw(filename);
            deleteCaffDataJpg(filename);

            LOGGER.info("Successful rollback caff data [{}]", filename);

            Thread.currentThread().interrupt();
            throw new CaffDataParsingException();
        }
    }
}
