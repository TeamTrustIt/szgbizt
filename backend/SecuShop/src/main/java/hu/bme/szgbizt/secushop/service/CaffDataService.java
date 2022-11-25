package hu.bme.szgbizt.secushop.service;

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service
@Transactional
public class CaffDataService implements ICaffDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffDataService.class);

    private static final String PATH_CAFF_DATA_RAW = "caffdata/raw";
    private static final Path ROOT = Paths.get(PATH_CAFF_DATA_RAW);

    private final CaffDataRepository caffDataRepository;
    private final ShopUserRepository shopUserRepository;

    /**
     * Instantiates a new {@link CaffDataRepository}.
     *
     * @param caffDataRepository The repository for {@link CaffDataEntity}.
     * @param shopUserRepository The repository for {@link ShopUserEntity}.
     */
    public CaffDataService(CaffDataRepository caffDataRepository, ShopUserRepository shopUserRepository) {
        this.caffDataRepository = caffDataRepository;
        this.shopUserRepository = shopUserRepository;
    }

    @Override
    public DetailedCaffData store(UUID callerUserId, String filename, String description, MultipartFile file) {
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
                    shopUserEntity
            );
            shopUserEntity.getCaffData().add(caffDataEntityToSave);

            Files.copy(file.getInputStream(), ROOT.resolve(Objects.requireNonNull(filename)));
            var savedCaffDataEntity = caffDataRepository.save(caffDataEntityToSave);

            return new DetailedCaffData(
                    savedCaffDataEntity.getId(),
                    savedCaffDataEntity.getName(),
                    savedCaffDataEntity.getDescription(),
                    savedCaffDataEntity.getPrice(),
                    savedCaffDataEntity.getShopUser().getUsername(),
                    "imageUrl", // todo
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

    @Override
    public List<DetailedCaffData> loadAll() {
        return caffDataRepository.findAll().stream()
                .map(caffDataEntity -> new DetailedCaffData(
                        caffDataEntity.getId(),
                        caffDataEntity.getName(),
                        caffDataEntity.getDescription(),
                        caffDataEntity.getPrice(),
                        caffDataEntity.getShopUser().getUsername(),
                        "imageUrl",
                        caffDataEntity.getUploadDate(),
                        List.of()))
                .collect(Collectors.toList());

        /*
        try {
            return Files.walk(ROOT, 1).filter(path -> !path.equals(ROOT)).map(ROOT::relativize);
        } catch (IOException ex) {
            LOGGER.error("Error while loading all caff data, exception: {}", ex.getMessage());
            throw new SecuShopInternalServerException();
        }
         */
    }

    @Override
    public DetailedCaffData load(UUID caffDataId) {
        return caffDataRepository.findById(caffDataId)
                .map(caffDataEntity -> new DetailedCaffData(
                        caffDataEntity.getId(),
                        caffDataEntity.getName(),
                        caffDataEntity.getDescription(),
                        caffDataEntity.getPrice(),
                        caffDataEntity.getShopUser().getUsername(),
                        "imageUrl",
                        caffDataEntity.getUploadDate(), List.of())
                )
                .orElseThrow(CaffDataNotFoundException::new);
    }

    @Override
    public Resource loadAsResource(UUID caffDataId) {

        var caffDataEntity = caffDataRepository.findById(caffDataId)
                .orElseThrow(CaffDataNotFoundException::new);
        var filename = caffDataEntity.getName();

        try {
            var file = ROOT.resolve(filename);
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

    @Override
    public void delete(UUID caffDataId) {

        var caffDataEntity = caffDataRepository.findById(caffDataId)
                .orElseThrow(CaffDataNotFoundException::new);
        var filename = caffDataEntity.getName();

        try {
            var path = ROOT.resolve(filename);

            caffDataRepository.delete(caffDataEntity);
            Files.delete(path);

        } catch (IOException e) {
            LOGGER.error("Error while deleting caff data [{}]", filename);
            throw new SecuShopInternalServerException();
        }
    }
}
