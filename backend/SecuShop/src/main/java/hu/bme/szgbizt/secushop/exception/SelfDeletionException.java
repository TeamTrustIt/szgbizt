package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0151;

public class SelfDeletionException extends SecuShopException {

    public SelfDeletionException() {
        super(SS_0151);
    }
}
