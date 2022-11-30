package hu.bme.szgbizt.secushop.exception;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0152;

public class NoAuthorityToProcessException extends SecuShopException {

    public NoAuthorityToProcessException() {
        super(SS_0152);
    }
}
