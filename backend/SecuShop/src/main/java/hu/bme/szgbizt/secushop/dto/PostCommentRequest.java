package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public final class PostCommentRequest {

    @NotNull(message = "Message cannot be null")
    private final String message;

    @JsonCreator
    public PostCommentRequest(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
