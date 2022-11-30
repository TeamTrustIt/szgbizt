package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class EmailNotUniqueException extends SecuShopException {

    public EmailNotUniqueException() {
        super(ErrorCode.SS_0121);
    }
}
