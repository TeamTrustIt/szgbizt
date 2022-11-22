package hu.bme.szgbizt.secushop.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class ShopUserEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "shopUser")
    private List<CaffDataEntity> caffData;

    @OneToMany(mappedBy = "shopUser")
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
     * @param balance  The balance of the {@link ShopUserEntity}.
     * @param caffData The {@link CaffDataEntity}.
     */
    public ShopUserEntity(BigDecimal balance, List<CaffDataEntity> caffData) {
        this.balance = balance;
        this.caffData = caffData;
        this.comments = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
