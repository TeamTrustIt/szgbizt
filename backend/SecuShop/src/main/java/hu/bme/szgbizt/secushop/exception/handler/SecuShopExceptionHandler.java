package hu.bme.szgbizt.secushop.exception.handler;

import hu.bme.szgbizt.secushop.dto.ErrorMessageResponse;
import hu.bme.szgbizt.secushop.exception.*;
import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
            return buildErrorMessage(ErrorCode.SS_0101, errorMessage);
        }

        return buildErrorMessage(ErrorCode.SS_0101);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildErrorMessage(ErrorCode.SS_0101, "Invalid input parameter(s)");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return buildErrorMessage(ErrorCode.SS_0100, ex.getMessage());
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

    @ExceptionHandler(SelfDeletionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleSelfDeletionException(SelfDeletionException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(CaffDataAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleCaffDataAlreadyExistException(CaffDataAlreadyExistException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(InvalidFileExtensionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageResponse handleInvalidFileExtensionException(InvalidFileExtensionException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorMessageResponse handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(CaffDataNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorMessageResponse handleCaffDataNotFoundException(CaffDataNotFoundException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorMessageResponse handleCommentNotFoundException(CommentNotFoundException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(NoAuthorityToProcessException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody ErrorMessageResponse handleNoAuthorityToProcessException(NoAuthorityToProcessException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(CaffDataParsingException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorMessageResponse handleCaffDataParsingException(CaffDataParsingException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    @ExceptionHandler(SecuShopInternalServerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorMessageResponse handleSecuShopInternalServerException(SecuShopInternalServerException ex) {
        return buildErrorMessage(ex.getErrorCode());
    }

    private ErrorMessageResponse buildErrorMessage(ErrorCode errorCode, String... errorMessage) {
        if (errorMessage.length == 0) {
            return new ErrorMessageResponse(errorCode.name(), errorCode.getMessage());
        }

        return new ErrorMessageResponse(errorCode.name(), errorMessage[0]);
    }
}
