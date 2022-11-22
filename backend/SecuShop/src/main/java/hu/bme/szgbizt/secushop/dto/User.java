package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class User {

    @NotNull(message = "Registered user cannot be null")
    private final RegisteredUser registeredUser;

    @NotNull(message = "Balance cannot be null")
    private final BigDecimal balance;

    public User(RegisteredUser registeredUser, BigDecimal balance) {
        this.registeredUser = registeredUser;
        this.balance = balance;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
