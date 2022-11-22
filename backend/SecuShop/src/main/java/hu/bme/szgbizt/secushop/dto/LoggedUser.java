package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object class for logged user.
 */
public final class LoggedUser {

    @NotNull(message = "Token cannot be null")
    private final String token;

    @NotNull(message = "user cannot be null")
    private final User user;

    /**
     * Instantiates a new {@link LoggedUser}.
     *
     * @param token The access token for the {@link LoggedUser}.
     * @param user  The details of the {@link LoggedUser}.
     */
    public LoggedUser(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
