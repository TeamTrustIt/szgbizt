package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public final class Comment {

    @NotNull(message = "Id cannot be null")
    private final UUID id;

    @NotNull(message = "Message cannot be null")
    private final String message;

    @NotNull(message = "Upload date cannot be null")
    private final Instant uploadDate;

    @NotNull(message = "User id cannot be null")
    private final UUID userId;

    @NotNull(message = "Caff data id cannot be null")
    private final UUID caffDataId;

    public Comment(UUID id, String message, Instant uploadDate, UUID userId, UUID caffDataId) {
        this.id = id;
        this.message = message;
        this.uploadDate = uploadDate;
        this.userId = userId;
        this.caffDataId = caffDataId;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getCaffDataId() {
        return caffDataId;
    }
}
