package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class UsernameNotUniqueException extends SecuShopException {

    public UsernameNotUniqueException() {
        super(ErrorCode.SS_0120);
    }
}
