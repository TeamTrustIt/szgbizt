package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
        LOGGER.info("Querying user [{}] by [{}]", userId, authentication.getName());
        var user = userService.getUser(userId);
        LOGGER.info("Successful queried user [{}] by [{}]", userId, authentication.getName());
        return user;
    }
}
