package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static hu.bme.szgbizt.secushop.util.Constant.REGEX_ONLY_LETTERS_AND_NUMBERS_AND_POINT;

/**
 * Dto class for post comment.
 */
@JsonInclude(NON_NULL)
public final class PostCommentRequest {

    @Pattern(regexp = REGEX_ONLY_LETTERS_AND_NUMBERS_AND_POINT, message = "Invalid character(s), every character should be letter or number")
    @NotNull(message = "Message cannot be null")
    private final String message;

    /**
     * Instantiates a new {@link PostCommentRequest}.
     *
     * @param message The message of the comment.
     */
    @JsonCreator
    public PostCommentRequest(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
