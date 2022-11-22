package hu.bme.szgbizt.secushop.security.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "black_listed_access_tokens")
public class BlackListedAccessTokenEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "date_of_inserted", nullable = false)
    private LocalDate dateOfInserted;

    /**
     * Instantiates a new {@link BlackListedAccessTokenEntity}.
     */
    public BlackListedAccessTokenEntity() {
        // Empty constructor.
    }

    /**
     * Instantiates a new {@link BlackListedAccessTokenEntity}.
     *
     * @param token The black listed access token.
     */
    public BlackListedAccessTokenEntity(String token) {
        this.token = token;
        this.dateOfInserted = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
