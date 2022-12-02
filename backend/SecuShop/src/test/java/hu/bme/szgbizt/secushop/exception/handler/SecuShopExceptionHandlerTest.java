package hu.bme.szgbizt.secushop.exception.handler;

import hu.bme.szgbizt.secushop.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.io.FileNotFoundException;
import java.util.Objects;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecuShopExceptionHandlerTest {

    private SecuShopExceptionHandler secuShopExceptionHandler;

    @BeforeEach
    void setUp() {
        secuShopExceptionHandler = new SecuShopExceptionHandler();
    }

    @Test
    void testHandleMethodArgumentNotValidExceptionWithoutFieldError() {

        // Arrange
        var methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(mock(BindingResult.class));
        when(methodArgumentNotValidException.getBindingResult().getFieldError()).thenReturn(null);

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0101.name(), errorMessageResponse.getErrorCode());
        assertEquals(SS_0101.getMessage(), errorMessageResponse.getMessage());
    }

    @Test
    void testHandleMethodArgumentNotValidExceptionWithFieldError() {

        // Arrange
        var methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(mock(BindingResult.class));
        when(methodArgumentNotValidException.getBindingResult().getFieldError()).thenReturn(mock(FieldError.class));
        when(Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError()).getDefaultMessage()).thenReturn("Hello there!");

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0101.name(), errorMessageResponse.getErrorCode());
        assertEquals("Hello there!", errorMessageResponse.getMessage());
    }

    @Test
    void testHandleHttpMessageNotReadableException() {

        // Arrange
        var httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleHttpMessageNotReadableException(httpMessageNotReadableException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0101.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleMissingServletRequestParameterException() {

        // Arrange
        var missingServletRequestParameterException = mock(MissingServletRequestParameterException.class);

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleMissingServletRequestParameterException(missingServletRequestParameterException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0100.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleUsernameNotUniqueException() {

        // Arrange
        var usernameNotUniqueException = new UsernameNotUniqueException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleUsernameNotUniqueException(usernameNotUniqueException);

        // Assert
        assertEquals(SS_0120.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleEmailNotUniqueException() {

        // Arrange
        var emailNotUniqueException = new EmailNotUniqueException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleEmailNotUniqueException(emailNotUniqueException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0121.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleSelfDeletionException() {

        // Arrange
        var selfDeletionException = new SelfDeletionException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleSelfDeletionException(selfDeletionException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0151.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleCaffDataAlreadyExistException() {

        // Arrange
        var caffDataAlreadyExistException = new CaffDataAlreadyExistException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleCaffDataAlreadyExistException(caffDataAlreadyExistException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0103.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleInvalidFileExtensionException() {

        // Arrange
        var invalidFileExtensionException = new InvalidFileExtensionException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleInvalidFileExtensionException(invalidFileExtensionException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0102.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandlePasswordMismatchException() {

        // Arrange
        var passwordMismatchException = new PasswordMismatchException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handlePasswordMismatchException(passwordMismatchException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0104.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleMaxUploadSizeExceededException() {

        // Arrange
        var maxUploadSizeExceededException = mock(MaxUploadSizeExceededException.class);

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleMaxUploadSizeExceededException(maxUploadSizeExceededException);

        // Assert
        assertEquals(SS_0101.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleMultipartException() {

        // Arrange
        var multipartException = mock(MultipartException.class);

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleMultipartException(multipartException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0102.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleUserNotFoundException() {

        // Arrange
        var userNotFoundException = new UserNotFoundException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleUserNotFoundException(userNotFoundException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0141.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleCaffDataNotFoundException() {

        // Arrange
        var caffDataNotFoundException = new CaffDataNotFoundException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleCaffDataNotFoundException(caffDataNotFoundException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0142.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleCommentNotFoundException() {

        // Arrange
        var commentNotFoundException = new CommentNotFoundException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleCommentNotFoundException(commentNotFoundException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0143.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleFileNotFoundException() {

        // Arrange
        var fileNotFoundException = mock(FileNotFoundException.class);

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleFileNotFoundException(fileNotFoundException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0142.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleNoAuthorityToProcessException() {

        // Arrange
        var noAuthorityToProcessException = new NoAuthorityToProcessException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleNoAuthorityToProcessException(noAuthorityToProcessException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0152.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleCaffDataParsingException() {

        // Arrange
        var caffDataParsingException = new CaffDataParsingException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleCaffDataParsingException(caffDataParsingException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0002.name(), errorMessageResponse.getErrorCode());
    }

    @Test
    void testHandleSecuShopInternalServerException() {

        // Arrange
        var secuShopInternalServerException = new SecuShopInternalServerException();

        // Act
        var errorMessageResponse = secuShopExceptionHandler.handleSecuShopInternalServerException(secuShopInternalServerException);

        // Assert
        assertNotNull(errorMessageResponse);
        assertEquals(SS_0001.name(), errorMessageResponse.getErrorCode());
    }
}
