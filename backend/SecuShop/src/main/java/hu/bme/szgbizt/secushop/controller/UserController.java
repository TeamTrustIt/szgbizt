package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.CaffData;
import hu.bme.szgbizt.secushop.dto.PutRegisteredUserRequest;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.exception.InvalidFileExtensionException;
import hu.bme.szgbizt.secushop.service.CaffDataService;
import hu.bme.szgbizt.secushop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_CAFF;
import static hu.bme.szgbizt.secushop.util.JwtHandler.getUserId;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController implements SecuShopBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final CaffDataService caffDataService;

    /**
     * Instantiates a new {@link UserService}.
     *
     * @param userService     The service class for user related processes.
     * @param caffDataService The service class for caff data related processes.
     */
    public UserController(UserService userService, CaffDataService caffDataService) {
        this.userService = userService;
        this.caffDataService = caffDataService;
    }

    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public User getUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Querying user [{}] by [{}]", userId, callerUserId);
        var user = userService.getUser(callerUserId, userId);
        LOGGER.info("Successful queried user [{}] by [{}]", userId, callerUserId);
        return user;
    }

    @PutMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RegisteredUser updateUser(Authentication authentication, @PathVariable("userId") UUID userId, @RequestBody PutRegisteredUserRequest putRegisteredUserRequest) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Updating user [{}] by [{}]", userId, callerUserId);
        var user = userService.updateUser(callerUserId, userId, putRegisteredUserRequest);
        LOGGER.info("Successful updated user [{}] by [{}]", userId, callerUserId);
        return user;
    }

    @DeleteMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting user [{}] by [{}]", userId, callerUserId);
        userService.deleteUser(callerUserId, userId);
        LOGGER.info("Successful deleted user [{}] by [{}]", userId, callerUserId);
    }

    /**
     * @DeleteMapping(value = "/caff-data/{caffDataId}")
     * @ResponseStatus(value = HttpStatus.NO_CONTENT)
     * public void deleteCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
     * var callerUserId = getUserId(authentication);
     * LOGGER.info("Deleting caff data [{}] by [{}]", caffDataId, callerUserId);
     * userService.deleteCaffData(callerUserId, caffDataId);
     * LOGGER.info("Successful deleted caff data [{}] by [{}]", caffDataId, callerUserId);
     * }
     */
    @DeleteMapping(value = "/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(Authentication authentication, @PathVariable("commentId") UUID commentId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting comment [{}] by [{}]", commentId, callerUserId);
        userService.deleteComment(callerUserId, commentId);
        LOGGER.info("Successful deleted comment [{}] by [{}]", commentId, callerUserId);
    }

    @PostMapping(value = "/caff-data")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody CaffData createCaffData(
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
        var caffData = caffDataService.store(callerUserId, filename, description, file);
        LOGGER.info("Successful uploaded caff data [{}] by [{}]", filename, callerUserId);
        return caffData;
    }

    @GetMapping(value = "/caff-data/{caffDataId}/caff")
    @ResponseStatus(value = HttpStatus.OK)
    public Resource getCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Downloading caff data [{}] by [{}]", caffDataId, callerUserId);
        var resource = caffDataService.loadAsResource(caffDataId);
        LOGGER.info("Successful downloaded caff data [{}] by [{}]", caffDataId, callerUserId);
        return resource;
    }

    @DeleteMapping(value = "/caff-data/{caffDataId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting caff data [{}] by [{}]", caffDataId, callerUserId);
        caffDataService.delete(caffDataId);
        LOGGER.info("Successful deleted caff data [{}] by [{}]", caffDataId, callerUserId);
    }
}
