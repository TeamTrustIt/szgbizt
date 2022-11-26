package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffData;
import hu.bme.szgbizt.secushop.dto.DetailedCaffData;
import hu.bme.szgbizt.secushop.exception.CaffDataAlreadyExistException;
import hu.bme.szgbizt.secushop.exception.CaffDataNotFoundException;
import hu.bme.szgbizt.secushop.exception.SecuShopInternalServerException;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.CaffDataRepository;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_JPG;
import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_RAW;
import static java.math.BigDecimal.ZERO;

@Service
@Transactional
public class SecuShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuShopService.class);

    private final CaffDataRepository caffDataRepository;
    private final ShopUserRepository shopUserRepository;

    /**
     * Instantiates a new {@link CaffDataRepository}.
     *
     * @param caffDataRepository The repository for {@link CaffDataEntity}.
     * @param shopUserRepository The repository for {@link ShopUserEntity}.
     */
    public SecuShopService(CaffDataRepository caffDataRepository, ShopUserRepository shopUserRepository) {
        this.caffDataRepository = caffDataRepository;
        this.shopUserRepository = shopUserRepository;
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

            Files.copy(file.getInputStream(), PATH_CAFF_DATA_RAW.resolve(Objects.requireNonNull(filename)));
            var savedCaffDataEntity = caffDataRepository.save(caffDataEntityToSave);

            return new DetailedCaffData(
                    savedCaffDataEntity.getId(),
                    savedCaffDataEntity.getName(),
                    savedCaffDataEntity.getDescription(),
                    savedCaffDataEntity.getPrice(),
                    savedCaffDataEntity.getShopUser().getUsername(),
                    buildImageUrl("1"), // todo
                    savedCaffDataEntity.getUploadDate(),
                    List.of());

        } catch (Exception ex) {

            if (ex instanceof CaffDataAlreadyExistException) {
                throw new CaffDataAlreadyExistException();
            }

            LOGGER.error("Error while saving caff data [{}], exception: {}", filename, ex.getMessage());
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
                        buildImageUrl("1"),
                        caffDataEntity.getUploadDate())
                )
                .collect(Collectors.toList());
    }

    public DetailedCaffData getCaffData(UUID caffDataId) {
        return caffDataRepository.findById(caffDataId)
                .map(caffDataEntity -> new DetailedCaffData(
                        caffDataEntity.getId(),
                        caffDataEntity.getName(),
                        caffDataEntity.getDescription(),
                        caffDataEntity.getPrice(),
                        caffDataEntity.getShopUser().getUsername(),
                        buildImageUrl("1"),
                        caffDataEntity.getUploadDate(), List.of())
                )
                .orElseThrow(CaffDataNotFoundException::new);
    }

    public Resource getCaffDataAsResource(UUID caffDataId) {

        var caffDataEntity = caffDataRepository.findById(caffDataId)
                .orElseThrow(CaffDataNotFoundException::new);
        var filename = caffDataEntity.getName();

        try {
            var file = PATH_CAFF_DATA_RAW.resolve(filename);
            var resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                LOGGER.error("Error while loading caff data [{}] as resource", filename);
                throw new SecuShopInternalServerException();
            }
        } catch (MalformedURLException ex) {
            LOGGER.error("Error while loading caff data [{}] as resource, exception: {}", filename, ex.getMessage());
            throw new SecuShopInternalServerException();
        }
    }

    private String buildImageUrl(String filename) {
        return "http://localhost:8080/api/v1/secu-shop/images/" + filename;
    }
}
