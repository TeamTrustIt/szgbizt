package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class MissingInputParameterException extends SecuShopException {

    public MissingInputParameterException() {
        super(ErrorCode.SS_0100);
    }
}
