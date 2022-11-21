package hu.bme.szgbizt.secushop.security.controller;

import hu.bme.szgbizt.secushop.controller.SecuShopBaseController;
import hu.bme.szgbizt.secushop.dto.PostRegistrationRequest;
import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.security.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public @ResponseBody String login(Authentication authentication) {
        LOGGER.info("Token requested for user [{}]", authentication.getName());
        var token = securityService.generateToken(authentication);
        LOGGER.info("Token granted for user [{}]", authentication.getName());
        return token;
    }

    @PostMapping(value = "/registration")
    public @ResponseBody User registration(@Valid @RequestBody PostRegistrationRequest postRegistrationRequest) {
        LOGGER.info("Registration in progress for user [{}]", postRegistrationRequest.getUsername());
        var registeredUser = securityService.registration(postRegistrationRequest);
        LOGGER.info("Successful registration for user [{}]", registeredUser.getUsername());
        return registeredUser;
    }
}
