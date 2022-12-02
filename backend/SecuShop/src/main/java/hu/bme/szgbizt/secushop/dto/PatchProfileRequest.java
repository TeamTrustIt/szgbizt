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
 * Dto class for modify user's username or email.
 */
@JsonPropertyOrder(value = {"username", "email"})
@JsonInclude(NON_NULL)
public final class PatchProfileRequest {

    @Pattern(regexp = REGEX_ONLY_LETTERS_AND_NUMBERS, message = "Invalid character(s), every character should be letter or number and the length must be at least 5 characters")
    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private final String email;

    /**
     * Instantiates a new {@link PatchProfileRequest}.
     *
     * @param username The username of the user to modify, has to be unique.
     * @param email    The email of the user to modify, has to be unique.
     */
    @JsonCreator
    public PatchProfileRequest(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email) {

        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
