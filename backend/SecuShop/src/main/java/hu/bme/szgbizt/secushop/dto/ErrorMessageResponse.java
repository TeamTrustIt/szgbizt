package hu.bme.szgbizt.secushop.dto;

import javax.validation.constraints.NotNull;

public final class ErrorMessageResponse {

    @NotNull(message = "Error code cannot be null")
    private final String errorCode;

    @NotNull(message = "Error message cannot be null")
    private final String message;

    public ErrorMessageResponse(String errorCode, String message) {
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
