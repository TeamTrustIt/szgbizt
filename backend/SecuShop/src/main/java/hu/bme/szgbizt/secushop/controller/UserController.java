package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.PutRegisteredUserRequest;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static hu.bme.szgbizt.secushop.util.JwtHandler.getUserId;
import static hu.bme.szgbizt.secushop.util.JwtHandler.getUsername;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController implements SecuShopBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Instantiates a new {@link UserService}.
     *
     * @param userService The service class for user related processes.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public User getUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        var callerUsername = getUsername(authentication);
        LOGGER.info("Querying user [{}] by [{}]", userId, callerUsername);
        var user = userService.getUser(userId);
        LOGGER.info("Successful queried user [{}] by [{}]", userId, callerUsername);
        return user;
    }

    @PutMapping(value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RegisteredUser updateUser(Authentication authentication, @PathVariable("userId") UUID userId, @RequestBody PutRegisteredUserRequest putRegisteredUserRequest) {
        var callerUsername = getUsername(authentication);
        var callerUserId = getUserId(authentication);
        LOGGER.info("Updating user [{}] by [{}]", userId, callerUsername);
        var user = userService.updateUser(callerUserId, userId, putRegisteredUserRequest);
        LOGGER.info("Successful updated user [{}] by [{}]", userId, callerUsername);
        return user;
    }
}
