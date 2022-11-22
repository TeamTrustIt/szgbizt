package hu.bme.szgbizt.secushop.exception.errorcode;

public enum ErrorCode {

    SS_0001("Internal server error"),
    SS_0100("Missing input parameter"),
    SS_0101("Invalid input parameter"),
    SS_0120("Username is not unique"),
    SS_0121("Email is not unique"),
    SS_0141("User not found"),
    SS_0151("Cannot delete yourself");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
