package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0104;

public class PasswordMismatchException extends SecuShopException{

    public PasswordMismatchException() {
        super(SS_0104);
    }
}
