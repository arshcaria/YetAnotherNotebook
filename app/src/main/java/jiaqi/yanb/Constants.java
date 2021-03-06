package jiaqi.yanb;

/**
 * Hold constants and enums that are used in this app
 */
public class Constants {
    public static final String EXTRA_KEY_NAME_EDIT_MODE = "extra_key_name_edit_mode";
    public static final String EXTRA_KEY_NAME_NOTE = "extra_key_name_note";

    public static final int ACTIVITY_REQUEST_CODE_CREATE = 0;
    public static final int ACTIVITY_REQUEST_CODE_MODIFY = 1;

    public static final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0;

    public enum EditMode {
        CREATE, MODIFY
    }
}
