package hu.bme.szgbizt.secushop.security.persistence.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "black_listed_access_token")
public class BlackListedAccessToken {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "token", nullable = false)
    private String token;

    /**
     * Instantiates a new {@link BlackListedAccessToken}.
     */
    public BlackListedAccessToken() {
        // Empty constructor.
    }

    /**
     * Instantiates a new {@link BlackListedAccessToken}.
     *
     * @param token The black listed access token.
     */
    public BlackListedAccessToken(String token) {
        this.token = token;
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
