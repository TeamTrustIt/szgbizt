package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public final class PutRegisteredUserRequest {

    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Password cannot be null")
    private final String password;

    @NotNull(message = "Email cannot be null")
    @Email
    private final String email;

    public PutRegisteredUserRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
