package hu.bme.szgbizt.secushop.exception.errorcode;

public enum ErrorCode {

    SS_0001("Internal server error"),
    SS_0002("Caff data parsing error"),
    SS_0100("Missing input parameter"),
    SS_0101("Invalid input parameter"),
    SS_0102("Invalid file extension"),
    SS_0103("Caff data with the specified name already exists"),
    SS_0104("Password mismatch"),
    SS_0105("Invalid character(s) in the filename, every character must be letter, number or '-' and the length must be between 1 and 30 characters"),
    SS_0120("Username is not unique"),
    SS_0121("Email is not unique"),
    SS_0122("Invalid current password"),
    SS_0141("User not found"),
    SS_0142("Caff data not found"),
    SS_0143("Comment not found"),
    SS_0151("Cannot delete yourself"),
    SS_0152("Cannot access or modify");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
