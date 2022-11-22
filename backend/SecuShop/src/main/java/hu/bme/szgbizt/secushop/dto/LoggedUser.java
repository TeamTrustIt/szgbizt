package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object class for logged user.
 */
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
    public LoggedUser(String token, RegisteredUser registeredUser) {
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
