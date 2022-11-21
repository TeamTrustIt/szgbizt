package hu.bme.szgbizt.secushop.exception.handler;

import hu.bme.szgbizt.secushop.dto.ErrorMessageResponse;
import hu.bme.szgbizt.secushop.exception.EmailNotUniqueException;
import hu.bme.szgbizt.secushop.exception.UsernameNotUniqueException;
import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.Objects.nonNull;

@ControllerAdvice
public class SecuShopExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var fieldError = ex.getBindingResult().getFieldError();

        if (nonNull(fieldError)) {
            var errorMessage = fieldError.getDefaultMessage();
            return buildErrorMessage(ErrorCode.SS_0100, errorMessage);
        }

        return buildErrorMessage(ErrorCode.SS_0100);
    }

    @ExceptionHandler(UsernameNotUniqueException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleUsernameNotUniqueException(UsernameNotUniqueException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(EmailNotUniqueException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleEmailNotUniqueException(EmailNotUniqueException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    private ErrorMessageResponse buildErrorMessage(ErrorCode errorCode, String... errorMessage) {
        if (errorMessage.length == 0) {
            return new ErrorMessageResponse(errorCode.name(), errorCode.getMessage());
        }

        return new ErrorMessageResponse(errorCode.name(), errorMessage[0]);
    }
}
