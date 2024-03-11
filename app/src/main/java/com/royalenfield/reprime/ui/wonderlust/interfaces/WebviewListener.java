package com.royalenfield.reprime.ui.wonderlust.interfaces;

public interface WebviewListener {
    void showLoginActivity();
    void finishWebView(String error);
    void chnageOrientation(String type);
    void navigateToScreen(String id);
    void showHideKeyboard(boolean isShown);
}
