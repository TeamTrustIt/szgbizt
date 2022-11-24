package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public final class CaffData {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @NotNull(message = "Filename cannot be null")
    private final String filename;

    @NotNull(message = "Description cannot be null")
    private final String description;

    @NotNull(message = "Price cannot be null")
    private final BigDecimal price;

    @NotNull(message = "Upload date cannot be null")
    private final Instant uploadDate;

    @NotNull(message = "User id cannot be null")
    private final UUID userId;

    private final String caffDataJpg;

    public CaffData(UUID id, String filename, String description, BigDecimal price, Instant uploadDate, UUID userId, String caffDataJpg) {
        this.id = id;
        this.filename = filename;
        this.description = description;
        this.price = price;
        this.uploadDate = uploadDate;
        this.userId = userId;
        this.caffDataJpg = caffDataJpg;
    }

    public UUID getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getDescrtiption() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCaffDataJpg() {
        return caffDataJpg;
    }
}
