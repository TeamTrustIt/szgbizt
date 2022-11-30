package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public abstract class SecuShopException extends RuntimeException {

    private final ErrorCode errorCode;

    protected SecuShopException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
