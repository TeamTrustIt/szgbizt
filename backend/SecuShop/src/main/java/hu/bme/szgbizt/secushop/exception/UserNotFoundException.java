package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0141;

public class UserNotFoundException extends SecuShopException {

    public UserNotFoundException() {
        super(SS_0141);
    }
}
