package hu.bme.szgbizt.secushop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController implements SecuShopBaseController {

    @GetMapping(value = "/home")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String helloWorld() {
        return "Hello World!";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/admin")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String helloAdmin() {
        return "Hello Admin!";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/user")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String helloUser() {
        return "Hello User!";
    }
}
