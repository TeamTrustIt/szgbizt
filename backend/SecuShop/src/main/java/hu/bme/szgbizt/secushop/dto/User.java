package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Dto class for user.
 */
@JsonInclude(NON_NULL)
public final class User {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private final String email;

    @NotNull(message = "Roles cannot be null")
    private final String roles;

    @NotNull(message = "Balance cannot be null")
    private final BigDecimal balance;

    @NotNull(message = "Caff data cannot be null")
    private final List<BaseCaffData> caffData;

    /**
     * Instantiates a new {@link User}.
     *
     * @param id       The identifier of the user.
     * @param username The username of the user.
     * @param email    The email of the user.
     * @param roles    The roles of the user.
     * @param balance  The balance of the user.
     * @param caffData The caff data of the user.
     */
    @JsonCreator
    public User(
            @JsonProperty("id") UUID id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("roles") String roles,
            @JsonProperty("balance") BigDecimal balance,
            @JsonProperty("caffData") List<BaseCaffData> caffData) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.balance = balance;
        this.caffData = caffData;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public List<BaseCaffData> getCaffData() {
        return caffData;
    }
}
