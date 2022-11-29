package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Dto class for caff comment.
 */
@JsonPropertyOrder(value = {"id", "message", "username", "caffDataId", "uploadDate"})
@JsonInclude(NON_NULL)
public final class CaffComment {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @NotNull(message = "Message cannot be null")
    private final String message;

    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Caff data id cannot be null")
    private final UUID caffDataId;

    @NotNull(message = "Upload date cannot be null")
    private final String uploadDate;

    /**
     * Instantiates a new {@link CaffComment}.
     *
     * @param id         The identifier of the caff comment.
     * @param message    The message from the caff comment.
     * @param username   The username of the author of the caff comment.
     * @param caffDataId The identifier of the caff data of the caff comment.
     * @param uploadDate The upload date of the caff comment.
     */
    @JsonCreator
    public CaffComment(
            @JsonProperty("id") UUID id,
            @JsonProperty("message") String message,
            @JsonProperty("username") String username,
            @JsonProperty("caffDataId") UUID caffDataId,
            @JsonProperty("uploadDate") String uploadDate) {

        this.id = id;
        this.message = message;
        this.username = username;
        this.caffDataId = caffDataId;
        this.uploadDate = uploadDate;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public UUID getCaffDataId() {
        return caffDataId;
    }

    public String getUploadDate() {
        return uploadDate;
    }
}
