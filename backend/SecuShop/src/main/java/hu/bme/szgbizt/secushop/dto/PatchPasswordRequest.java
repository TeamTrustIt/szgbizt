package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static hu.bme.szgbizt.secushop.util.Constant.REGEX_PASSWORD;

/**
 * Dto class for modify user's password.
 */
@JsonPropertyOrder(value = {"currentPassword", "newPassword"})
@JsonInclude(NON_NULL)
public final class PatchPasswordRequest {

    @Pattern(regexp = REGEX_PASSWORD, message = "Invalid current password, every character must be letter and the length must be at least 8 characters")
    @NotNull(message = "Current password cannot be null")
    private final String currentPassword;

    @Pattern(regexp = REGEX_PASSWORD, message = "Invalid new password, every character must be letter and the length must be at least 8 characters")
    @NotNull(message = "New password cannot be null")
    private final String newPassword;

    /**
     * Instantiates a new {@link PatchPasswordRequest}.
     *
     * @param currentPassword The current password of the user.
     * @param newPassword     The new password of the user.
     */
    @JsonCreator
    public PatchPasswordRequest(
            @JsonProperty("currentPassword") String currentPassword,
            @JsonProperty("newPassword") String newPassword) {

        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
