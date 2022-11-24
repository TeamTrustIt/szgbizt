package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class SecuShopInternalServerException extends SecuShopException {

    public SecuShopInternalServerException() {
        super(ErrorCode.SS_0001);
    }
}
