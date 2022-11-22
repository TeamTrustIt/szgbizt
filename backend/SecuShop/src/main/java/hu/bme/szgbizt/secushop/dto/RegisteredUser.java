package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public final class RegisteredUser {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private final String email;

    @NotNull(message = "Roles cannot be null")
    private final String roles;

    public RegisteredUser(UUID id, String username, String email, String roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRoles() {
        return roles;
    }
}
