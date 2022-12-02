package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0105;

/**
 * Exception class for invalid filename format.
 */
public class InvalidFilenameException extends SecuShopException {

    public InvalidFilenameException() {
        super(SS_0105);
    }
}
