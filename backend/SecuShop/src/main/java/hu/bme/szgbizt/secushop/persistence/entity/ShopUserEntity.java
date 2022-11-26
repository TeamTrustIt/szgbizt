package hu.bme.szgbizt.secushop.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "users")
public class ShopUserEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "username", nullable = false, length = 30, unique = true)
    private String username;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(fetch = EAGER, mappedBy = "shopUser", cascade = REMOVE)
    private List<CaffDataEntity> caffData;

    @OneToMany(fetch = EAGER, mappedBy = "shopUser", cascade = REMOVE)
    private List<CommentEntity> comments;

    /**
     * Instantiates a new {@link ShopUserEntity}.
     */
    public ShopUserEntity() {
        // Empty constructor.
    }

    /**
     * Instantiates a new {@link ShopUserEntity}.
     *
     * @param id       The identifier of the {@link ShopUserEntity}.
     * @param username The username of the {@link ShopUserEntity}.
     * @param balance  The balance of the {@link ShopUserEntity}.
     */
    public ShopUserEntity(UUID id, String username, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        this.caffData = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<CaffDataEntity> getCaffData() {
        return caffData;
    }

    public void setCaffData(List<CaffDataEntity> caffData) {
        this.caffData = caffData;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }
}
