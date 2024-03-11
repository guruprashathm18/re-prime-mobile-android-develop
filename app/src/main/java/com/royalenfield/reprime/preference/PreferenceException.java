package com.royalenfield.reprime.preference;

/**
 * @author BOP1KOR on 2/8/2019.
 */

public class PreferenceException extends Exception {
    public static final String PREFERENCE_NOT_FOUND = "Preferences not found";
    public static final String PREFERENCE_EDIT_PROHIBITED = "Editing the preferences is not allowed";

    /**
     * Constructor with a message.
     *
     * @param message The message.
     */
    public PreferenceException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and a throwable.
     *
     * @param message The message.
     * @param cause   The cause for the exception.
     */
    public PreferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
