package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0142;

public class CaffDataNotFoundException extends SecuShopException {

    public CaffDataNotFoundException() {
        super(SS_0142);
    }
}
