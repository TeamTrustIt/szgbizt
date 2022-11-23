package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class NoAuthorityToProcessException extends SecuShopException {

    public NoAuthorityToProcessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
