package hu.bme.szgbizt.secushop.exception;

import hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode;

public class CommentNotFoundException extends SecuShopException {

    public CommentNotFoundException() {
        super(ErrorCode.SS_0143);
    }
}
