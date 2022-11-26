package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffComment;
import hu.bme.szgbizt.secushop.dto.CaffData;
import hu.bme.szgbizt.secushop.dto.DetailedCaffData;
import hu.bme.szgbizt.secushop.exception.*;
import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.CommentRepository;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_JPG;
import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_RAW;
import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_CAFF;
import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_JSON;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;

@Service
@Transactional
public class SecuShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuShopService.class);

    private final CaffDataRepository caffDataRepository;
    private final ShopUserRepository shopUserRepository;
    private final CommentRepository commentRepository;

    /**
     * Instantiates a new {@link CaffDataRepository}.
     *
     * @param caffDataRepository The repository for {@link CaffDataEntity}.
     * @param shopUserRepository The repository for {@link ShopUserEntity}.
     * @param commentRepository  The repository for {@link CommentEntity}.
     */
    @Autowired
    public SecuShopService(CaffDataRepository caffDataRepository, ShopUserRepository shopUserRepository, CommentRepository commentRepository) {
        this.caffDataRepository = caffDataRepository;
        this.shopUserRepository = shopUserRepository;
        this.commentRepository = commentRepository;
    }

    public UrlResource getImage(String filename) {
        var path = Paths.get(PATH_CAFF_DATA_JPG + "/" + filename + ".jpg");

        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException ex) {
            LOGGER.error("Something went wrong under the image [{}] downloading, error: {}", filename, ex);
            throw new SecuShopInternalServerException();
        }
    }

    public DetailedCaffData createCaffData(UUID callerUserId, String filename, String description, MultipartFile file) {
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
                    buildImageUrl(filename),
                    shopUserEntity
            );
            shopUserEntity.getCaffData().add(caffDataEntityToSave);

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
                    savedCaffDataEntity.getUploadDate(),
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
                        caffDataEntity.getUploadDate())
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
                                            commentEntity.getUploadDate())
                                    )
                                    .collect(Collectors.toList());

                            return new DetailedCaffData(
                                    caffDataEntity.getId(),
                                    caffDataEntity.getName(),
                                    caffDataEntity.getDescription(),
                                    caffDataEntity.getPrice(),
                                    caffDataEntity.getShopUser().getUsername(),
                                    caffDataEntity.getImageUrl(),
                                    caffDataEntity.getUploadDate(),
                                    caffComments
                            );
                        }
                )
                .orElseThrow(CaffDataNotFoundException::new);
    }

    public Resource getCaffDataAsResource(UUID caffDataId) {

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
                savedCommentEntity.getUploadDate()
        );
    }

    private String buildImageUrl(String filename) {
        return "http://localhost:8080/api/v1/secu-shop/images/" + filename + "_ciff0";
    }

    private void parseCaffData(String filename) {

        try {
            LOGGER.info("Parsing caff file [{}]", filename);

            var userDirectory = System.getProperty("user.dir");
            var caffDataDirectory = userDirectory + "\\caffdata";
            var fileExe = caffDataDirectory + "\\caff_parser.exe";
            var inputFile = ".\\raw\\" + filename + FILE_EXTENSION_CAFF;
            var outputFile = ".\\jpg\\" + filename + FILE_EXTENSION_JSON;
            var directory = new File(caffDataDirectory);
            var process = Runtime.getRuntime().exec(fileExe + " " + inputFile + " " + outputFile, null, directory);
            var exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new CaffDataParsingException();
            }

        } catch (IOException | InterruptedException | CaffDataParsingException ex) {
            try {
                LOGGER.error("Something went wrong under the caff data [{}] parsing, error: {}", filename, ex.getMessage());

                LOGGER.info("Try to rollback caff data [{}] automatically", filename);
                var pathRaw = PATH_CAFF_DATA_RAW.resolve(filename);
                Files.delete(pathRaw);
                LOGGER.info("Successful rollback caff data [{}]", filename);
            } catch (IOException e) {
                LOGGER.info("Something went wrong under the caff data [{}] rollback", filename);
            }

            Thread.currentThread().interrupt();
            throw new CaffDataParsingException();
        }
    }
}
