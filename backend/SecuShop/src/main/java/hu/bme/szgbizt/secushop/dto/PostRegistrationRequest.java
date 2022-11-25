package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static hu.bme.szgbizt.secushop.util.Constant.REGEX_ONLY_LETTERS_AND_NUMBERS;

/**
 * Dto class for registration.
 */
@JsonPropertyOrder(value = {"username", "password", "email"})
@JsonInclude(NON_NULL)
public final class PostRegistrationRequest {

    @Pattern(regexp = REGEX_ONLY_LETTERS_AND_NUMBERS, message = "Invalid character(s), every character should be letter or number")
    @NotNull(message = "Username cannot be null")
    private final String username;

    @Pattern(regexp = REGEX_ONLY_LETTERS_AND_NUMBERS, message = "Invalid character(s), every character should be letter or number")
    @NotNull(message = "Password cannot be null")
    private final String password;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private final String email;

    /**
     * Instantiates a new {@link PostRegistrationRequest}.
     *
     * @param username The username of the user to register, has to be unique.
     * @param password The password of the user to register.
     * @param email    The email of the user to register, has to be unique.
     */
    @JsonCreator
    public PostRegistrationRequest(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email) {

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
