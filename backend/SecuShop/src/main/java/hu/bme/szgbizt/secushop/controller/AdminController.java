package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.DetailedUser;
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

import static hu.bme.szgbizt.secushop.util.JwtHandler.getUserId;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController implements ISecuShopBaseController {

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
        var callerUserId = getUserId(authentication);
        LOGGER.info("Querying all users by [{}]", callerUserId);
        var registeredUsers = adminService.getUsers();
        LOGGER.info("Successful queried all users by [{}]", callerUserId);
        return registeredUsers;
    }

    @GetMapping(value = "/admin/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody DetailedUser getUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Querying user [{}] by [{}]", userId, callerUserId);
        var user = adminService.getUser(userId);
        LOGGER.info("Successful queried user [{}] by [{}]", userId, callerUserId);
        return user;
    }

    @DeleteMapping(value = "/admin/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication, @PathVariable("userId") UUID userId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting user [{}] by [{}]", userId, callerUserId);
        adminService.deleteUser(callerUserId, userId);
        LOGGER.info("Successful deleted user [{}] by [{}]", userId, callerUserId);
    }

    @DeleteMapping(value = "/admin/caff-data/{caffDataId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCaffData(Authentication authentication, @PathVariable("caffDataId") UUID caffDataId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting caff data [{}] by [{}]", caffDataId, callerUserId);
        adminService.deleteCaffData(caffDataId);
        LOGGER.info("Successful deleted caff data [{}] by [{}]", caffDataId, callerUserId);
    }

    @DeleteMapping(value = "/admin/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(Authentication authentication, @PathVariable("commentId") UUID commentId) {
        var callerUserId = getUserId(authentication);
        LOGGER.info("Deleting comment [{}] by [{}]", commentId, callerUserId);
        adminService.deleteComment(commentId);
        LOGGER.info("Successful deleted comment [{}] by [{}]", commentId, callerUserId);
    }
}

