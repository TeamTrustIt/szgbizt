package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0103;

public class CaffDataAlreadyExistException extends SecuShopException {

    public CaffDataAlreadyExistException() {
        super(SS_0103);
    }
}
