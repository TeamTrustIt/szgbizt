package hu.bme.szgbizt.secushop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Dto class for error message response.
 */
@JsonPropertyOrder(value = {"errorCode", "message"})
@JsonInclude(NON_NULL)
public final class ErrorMessageResponse {

    @NotNull(message = "Error code cannot be null")
    private final String errorCode;

    @NotNull(message = "Error message cannot be null")
    private final String message;

    /**
     * Instantiates a new {@link ErrorMessageResponse}.
     *
     * @param errorCode The code of the error.
     * @param message   The message of the error.
     */
    @JsonCreator
    public ErrorMessageResponse(
            @JsonProperty("errorCode") String errorCode,
            @JsonProperty("message") String message) {

        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
