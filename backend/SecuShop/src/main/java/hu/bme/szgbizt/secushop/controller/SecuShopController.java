package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.DetailedCaffData;
import hu.bme.szgbizt.secushop.exception.InvalidFileExtensionException;
import hu.bme.szgbizt.secushop.service.SecuShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_CAFF;
import static hu.bme.szgbizt.secushop.util.JwtHandler.getUserId;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class SecuShopController implements ISecuShopBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuShopController.class);

    private final SecuShopService secuShopService;

    /**
     * Instantiates a new {@link SecuShopController}.
     *
     * @param secuShopService The service class for base processes.
     */
    protected SecuShopController(SecuShopService secuShopService) {
        this.secuShopService = secuShopService;
    }

    @GetMapping(value = "/images/{filename}", produces = IMAGE_JPEG_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody UrlResource getImage(Authentication authentication, @PathVariable("filename") String filename) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Downloading image [{}] by [{}]", filename, callerUserId);
        var urlResource = secuShopService.getImage(filename);
        LOGGER.info("Successful downloaded image [{}] by [{}]", filename, callerUserId);
        return urlResource;
    }

    @GetMapping(value = "/caff-data/{caffDataId}/caff")
    @ResponseStatus(value = HttpStatus.OK)
    public Resource getCaffDataAsResource(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Downloading caff data [{}] by [{}]", caffDataId, callerUserId);
        var resource = secuShopService.loadAsResource(caffDataId);
        LOGGER.info("Successful downloaded caff data [{}] by [{}]", caffDataId, callerUserId);
        return resource;
    }

    @GetMapping(value = "/caff-data/{caffDataId}")
    @ResponseStatus(value = HttpStatus.OK)
    public DetailedCaffData getCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Querying caff data [{}] by [{}]", caffDataId, callerUserId);
        var caffData = secuShopService.load(caffDataId);
        LOGGER.info("Successful queried caff data [{}] by [{}]", caffDataId, callerUserId);
        return caffData;
    }

    @GetMapping(value = "/caff-data")
    @ResponseStatus(value = HttpStatus.OK)
    public List<DetailedCaffData> getCaffDataList(Authentication authentication) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Querying all caff data by [{}]", callerUserId);
        var caffDataList = secuShopService.loadAll();
        LOGGER.info("Successful queried all caff data by [{}]", callerUserId);
        return caffDataList;
    }

    @PostMapping(value = "/caff-data")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody DetailedCaffData createCaffData(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename,
            @RequestParam("description") String description) {

        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(FILE_EXTENSION_CAFF)) {
            LOGGER.error("Invalid file extension");
            throw new InvalidFileExtensionException();
        }

        var callerUserId = getUserId(authentication);
        LOGGER.info("Uploading caff data [{}] by [{}]", filename, callerUserId);
        var caffData = secuShopService.store(callerUserId, filename, description, file);
        LOGGER.info("Successful uploaded caff data [{}] by [{}]", filename, callerUserId);
        return caffData;
    }
}
