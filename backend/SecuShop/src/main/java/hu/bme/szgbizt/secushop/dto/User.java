package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    public User(UUID id, String username, String email, String roles, BigDecimal balance, List<BaseCaffData> caffData) {
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
