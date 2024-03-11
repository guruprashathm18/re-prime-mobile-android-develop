package com.royalenfield.reprime.ui.phoneconfigurator.listener;

public interface ShowLoginListener {
    void showLoginActivity();
    void gotoSavedConfigScreen();
    void finishWebView(String error);
    void chnageOrientation(String type);
}
