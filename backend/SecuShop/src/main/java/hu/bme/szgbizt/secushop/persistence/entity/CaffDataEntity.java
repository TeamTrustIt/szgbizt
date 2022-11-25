package hu.bme.szgbizt.secushop.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "caff_data")
public class CaffDataEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "upload_date", nullable = false)
    private Instant uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ShopUserEntity shopUser;

    @OneToMany(fetch = EAGER, mappedBy = "caffData")
    private List<CommentEntity> comments;

    /**
     * Instantiates a new {@link CaffDataEntity}.
     */
    public CaffDataEntity() {
        // Empty constructor.
    }

    /**
     * Instantiates a new {@link CaffDataEntity}.
     *
     * @param name        The name of the file.
     * @param description The description of the file.
     * @param price       The price of the file.
     * @param shopUser    The user who belongs to the caff data.
     */
    public CaffDataEntity(String name, String description, BigDecimal price, ShopUserEntity shopUser) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.uploadDate = Instant.now();
        this.shopUser = shopUser;
        this.comments = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ShopUserEntity getShopUser() {
        return shopUser;
    }

    public void setShopUser(ShopUserEntity shopUser) {
        this.shopUser = shopUser;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }
}
