package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Dto class for caff data without comments.
 */
@JsonPropertyOrder(value = {"id", "filename", "description", "price", "username", "imageUrl", "uploadDate"})
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

    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Image url cannot be null")
    private final String imageUrl;

    @NotNull(message = "Upload date cannot be null")
    private final String uploadDate;

    /**
     * Instantiates a new {@link CaffData}.
     *
     * @param id          The identifier of the detailed caff data.
     * @param filename    The filename of the detailed caff data.
     * @param description The description of the detailed caff data.
     * @param price       The price of the detailed caff data.
     * @param username    The username of the owner of the detailed caff data.
     * @param imageUrl    The image url of the detailed caff data.
     * @param uploadDate  The upload data of the detailed caff data.
     */
    @JsonCreator
    public CaffData(
            @JsonProperty("id") UUID id,
            @JsonProperty("filename") String filename,
            @JsonProperty("description") String description,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("username") String username,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("uploadDate") String uploadDate) {

        this.id = id;
        this.filename = filename;
        this.description = description;
        this.price = price;
        this.username = username;
        this.imageUrl = imageUrl;
        this.uploadDate = uploadDate;
    }

    public UUID getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }
}
