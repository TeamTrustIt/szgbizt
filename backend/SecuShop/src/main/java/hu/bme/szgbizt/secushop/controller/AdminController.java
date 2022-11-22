package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.RegisteredUser;
import hu.bme.szgbizt.secushop.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController implements SecuShopBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    /**
     * Instantiates a new {@link AdminController}.
     *
     * @param adminService The service class for admin related processes.
     */
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "/admin/users")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<RegisteredUser> getUsers(Authentication authentication) {
        LOGGER.info("Querying all users by [{}]", authentication.getName());
        var registeredUsers = adminService.getUsers();
        LOGGER.info("Successful queried all users by [{}]", authentication.getName());
        return registeredUsers;
    }

    @DeleteMapping(value = "/admin/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        LOGGER.info("Deleting user [{}] by [{}]", userId, authentication.getName());
        adminService.deleteUser(authentication, userId);
        LOGGER.info("Successful deleted user [{}] by [{}]", userId, authentication.getName());
    }

    @DeleteMapping(value = "/admin/caff-data/{caffDataId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        LOGGER.info("Deleting caff data [{}] by [{}]", caffDataId, authentication.getName());
        adminService.deleteCaffData(caffDataId);
        LOGGER.info("Successful deleted caff data [{}] by [{}]", caffDataId, authentication.getName());
    }
}

