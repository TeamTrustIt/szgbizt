package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class InvalidFileExtensionException extends SecuShopException {

    public InvalidFileExtensionException() {
        super(ErrorCode.SS_0102);
    }
}
