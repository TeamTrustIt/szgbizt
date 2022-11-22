package hu.bme.szgbizt.secushop.security.controller;

import hu.bme.szgbizt.secushop.controller.SecuShopBaseController;
import hu.bme.szgbizt.secushop.dto.LoggedUser;
import hu.bme.szgbizt.secushop.dto.PostRegistrationRequest;
import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.security.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SecurityController implements SecuShopBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

    private final SecurityService securityService;

    /**
     * Instantiates a new {@link SecurityController}.
     *
     * @param securityService The {@link SecurityService}.
     */
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping(value = "/login")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody LoggedUser login(Authentication authentication) {
        LOGGER.info("Login in progress with user [{}]", authentication.getName());
        var loggedUser = securityService.login(authentication);
        LOGGER.info("Successful login with user [{}]", authentication.getName());
        return loggedUser;
    }

    @PostMapping(value = "/registration")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody RegisteredUser registration(@Valid @RequestBody PostRegistrationRequest postRegistrationRequest) {
        LOGGER.info("Registration in progress for user [{}]", postRegistrationRequest.getUsername());
        var registeredUser = securityService.registration(postRegistrationRequest);
        LOGGER.info("Successful registration for user [{}]", registeredUser.getUsername());
        return registeredUser;
    }

    @PostMapping(value = "/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(Authentication authentication) {
        LOGGER.info("Logout in progress with user [{}]", authentication.getName());
        securityService.logout(authentication);
        LOGGER.info("Successful logout with user [{}]", authentication.getName());
    }
}
