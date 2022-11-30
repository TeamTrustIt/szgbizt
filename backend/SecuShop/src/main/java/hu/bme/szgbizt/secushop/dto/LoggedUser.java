package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Dto class for logged user.
 */
@JsonPropertyOrder(value = {"token", "registeredUser"})
@JsonInclude(NON_NULL)
public final class LoggedUser {

    @NotNull(message = "Token cannot be null")
    private final String token;

    @NotNull(message = "Registered user cannot be null")
    private final RegisteredUser registeredUser;

    /**
     * Instantiates a new {@link LoggedUser}.
     *
     * @param token          The access token for the {@link LoggedUser}.
     * @param registeredUser The details of the {@link LoggedUser}.
     */
    @JsonCreator
    public LoggedUser(
            @JsonProperty("token") String token,
            @JsonProperty("registeredUser") RegisteredUser registeredUser) {

        this.token = token;
        this.registeredUser = registeredUser;
    }

    public String getToken() {
        return token;
    }

    public RegisteredUser getUser() {
        return registeredUser;
    }
}
