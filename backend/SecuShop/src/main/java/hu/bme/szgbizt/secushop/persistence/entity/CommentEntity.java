package hu.bme.szgbizt.secushop.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "message")
    private String message;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ShopUserEntity shopUser;

    @ManyToOne
    @JoinColumn(name = "caff_data_id")
    private CaffDataEntity caffData;

    /**
     * Instantiates a new {@link CommentEntity}.
     */
    public CommentEntity() {
        // Empty constructor.
    }

    /**
     * Instantiates a new {@link CommentEntity}.
     *
     * @param message  The message from the comment.
     * @param shopUser The user who published the comment.
     * @param caffData The caff data which belongs to the comment.
     */
    public CommentEntity(String message, ShopUserEntity shopUser, CaffDataEntity caffData) {
        this.message = message;
        this.uploadDate = LocalDateTime.now();
        this.shopUser = shopUser;
        this.caffData = caffData;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ShopUserEntity getShopUser() {
        return shopUser;
    }

    public void setShopUser(ShopUserEntity shopUser) {
        this.shopUser = shopUser;
    }

    public CaffDataEntity getCaffData() {
        return caffData;
    }

    public void setCaffData(CaffDataEntity caffData) {
        this.caffData = caffData;
    }
}
