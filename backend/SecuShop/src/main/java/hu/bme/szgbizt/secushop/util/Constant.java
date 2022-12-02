package hu.bme.szgbizt.secushop.util;

/**
 * Class for constant values in the application.
 */
public final class Constant {

    public static final String SYSTEM_ID = "secu-shop";
    public static final String SYSTEM_BASE_URL = "/api/v1/secu-shop";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLES = "roles";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String FILE_EXTENSION_CAFF = ".caff";
    public static final String FILE_EXTENSION_JSON = ".json";
    public static final String REGEX_ONLY_LETTERS_AND_NUMBERS = "^[a-zA-Z0-9]{5,}*$";
    public static final String REGEX_PASSWORD = "^[a-zA-Z]{8,}$";
    public static final String REGEX_COMMENT = "^[a-zA-Z0-9.!?, ]{1,}$";
    public static final String REGEX_FILENAME = "^[a-zA-Z0-9-]{1,30}$";

    private Constant() {
        // Empty constructor.
    }
}
