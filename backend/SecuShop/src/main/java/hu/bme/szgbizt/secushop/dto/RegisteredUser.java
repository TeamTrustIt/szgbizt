package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static hu.bme.szgbizt.secushop.util.Constant.REGEX_ONLY_LETTERS_AND_NUMBERS;

/**
 * Dto class for registered user.
 */
@JsonPropertyOrder(value = {"id", "username", "email", "roles"})
@JsonInclude(NON_NULL)
public final class RegisteredUser {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @Pattern(regexp = REGEX_ONLY_LETTERS_AND_NUMBERS, message = "Invalid character(s) in the username, every character should be letter or number and the length must be at least 4 characters")
    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private final String email;

    @NotNull(message = "Roles cannot be null")
    private final String roles;

    /**
     * Instantiates a new {@link RegisteredUser}.
     *
     * @param id       The identifier of the registered user.
     * @param username The username of the registered user.
     * @param email    The email of the registered user.
     * @param roles    The roles of the registered user.
     */
    @JsonCreator
    public RegisteredUser(
            @JsonProperty("id") UUID id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("roles") String roles) {

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
