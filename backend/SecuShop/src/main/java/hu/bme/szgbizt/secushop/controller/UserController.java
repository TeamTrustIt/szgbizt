package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.*;
import hu.bme.szgbizt.secushop.service.SecuShopService;
import hu.bme.szgbizt.secushop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final SecuShopService secuShopService;

    /**
     * Instantiates a new {@link UserService}.
     *
     * @param userService     The service class for user related processes.
     * @param secuShopService The service class for caff data related processes.
     */
    public UserController(UserService userService, SecuShopService secuShopService) {
        this.userService = userService;
        this.secuShopService = secuShopService;
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

    @PutMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RegisteredUser updateUser(
            Authentication authentication,
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody PatchProfileRequest putRegisteredUserRequest) {

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

    @PostMapping(value = "/caff-data/{caffDataId}/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody CaffComment createComment(
            Authentication authentication,
            @PathVariable("caffDataId") UUID caffDataId,
            @Valid @RequestBody PostCommentRequest postCommentRequest) {

        var callerUserId = getUserId(authentication);
        LOGGER.info("Posting comment to caff data [{}] by [{}]", caffDataId, callerUserId);
        var comment = userService.postComment(callerUserId, caffDataId, postCommentRequest.getMessage());
        LOGGER.info("Successful posted comment to caff data [{}] by [{}]", caffDataId, callerUserId);
        return comment;
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
}
