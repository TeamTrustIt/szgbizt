package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public final class BaseCaffData {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @NotNull(message = "Name cannot be null")
    private final String name;

    @NotNull(message = "Description cannot be null")
    private final String description;

    @PositiveOrZero
    private final BigDecimal price;

    private final Instant uploadDate;

    private final String uploader;

    private final List<String> comments;

    public BaseCaffData(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = null;
        this.uploadDate = null;
        this.uploader = null;
        this.comments = null;
    }

    public BaseCaffData(UUID id, String name, String description, BigDecimal price, Instant uploadDate, String uploader, List<String> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.uploadDate = uploadDate;
        this.uploader = uploader;
        this.comments = comments;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public String getUploader() {
        return uploader;
    }

    public List<String> getComments() {
        return comments;
    }
}
