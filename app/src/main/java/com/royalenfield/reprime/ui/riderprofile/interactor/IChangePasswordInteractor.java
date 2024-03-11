package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.ui.riderprofile.listeners.ChangePasswordListener;

public interface IChangePasswordInteractor {
    void onChangePassword(String previousPassword, String newPassword, String confirmPassword, ChangePasswordListener changePasswordListener);
}
