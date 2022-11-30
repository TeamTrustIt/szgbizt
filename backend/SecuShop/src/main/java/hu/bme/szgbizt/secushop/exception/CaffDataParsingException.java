package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0002;

/**
 * Exception class for caff data parsing error.
 */
public class CaffDataParsingException extends SecuShopException {

    public CaffDataParsingException() {
        super(SS_0002);
    }
}
