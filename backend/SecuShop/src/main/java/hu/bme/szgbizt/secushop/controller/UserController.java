package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.DetailedUser;
import hu.bme.szgbizt.secushop.dto.PatchPasswordRequest;
import hu.bme.szgbizt.secushop.dto.PatchProfileRequest;
import hu.bme.szgbizt.secushop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.util.JwtHandler.getUserId;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController implements ISecuShopBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Instantiates a new {@link UserService}.
     *
     * @param userService The service class for user related processes.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public DetailedUser getUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Querying user [{}] by [{}]", userId, callerUserId);
        var user = userService.getUser(callerUserId, userId);
        LOGGER.info("Successful queried user [{}] by [{}]", userId, callerUserId);
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

    @DeleteMapping(value = "/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(Authentication authentication, @PathVariable("commentId") UUID commentId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting comment [{}] by [{}]", commentId, callerUserId);
        userService.deleteComment(callerUserId, commentId);
        LOGGER.info("Successful deleted comment [{}] by [{}]", commentId, callerUserId);
    }

    @DeleteMapping(value = "/caff-data/{caffDataId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting caff data [{}] by [{}]", caffDataId, callerUserId);
        userService.deleteCaffData(callerUserId, caffDataId);
        LOGGER.info("Successful deleted caff data [{}] by [{}]", caffDataId, callerUserId);
    }

    @PatchMapping(value = "/users/{userId}/password")
    @ResponseStatus(value = HttpStatus.OK)
    public void modifyPassword(
            Authentication authentication,
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody PatchPasswordRequest patchPasswordRequest) {

        var callerUserId = getUserId(authentication);
        LOGGER.info("Modify user password by [{}]", userId);
        userService.modifyPassword(callerUserId, userId, patchPasswordRequest);
        LOGGER.info("Successful modified user password by [{}]", callerUserId);
    }

    @PatchMapping(value = "/users/{userId}/profile")
    @ResponseStatus(value = HttpStatus.OK)
    public void modifyProfile(
            Authentication authentication,
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody PatchProfileRequest patchProfileRequest) {

        var callerUserId = getUserId(authentication);
        LOGGER.info("Modify user profile by [{}]", userId);
        userService.modifyProfile(callerUserId, userId, patchProfileRequest);
        LOGGER.info("Successful modified user profile by [{}]", callerUserId);
    }
}
