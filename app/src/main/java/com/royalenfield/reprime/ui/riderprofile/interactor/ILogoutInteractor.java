package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.ui.riderprofile.listeners.OnLogoutResponseListener;

/**
 * An Interactor helps models cross application boundaries such as networks or serialization
 * This ILogoutInteractor{@link LogoutInteractor} knows nothing about a UI or the {@link com.royalenfield.reprime.ui.riderprofile.presenter.LogoutPresenter}
 */

public interface ILogoutInteractor {

    /**
     * Api call to call logout .
     *
     * @param onLogoutResponseListener To communicate to presenter.
     */
    void logout(OnLogoutResponseListener onLogoutResponseListener);

    void forgotMe(OnLogoutResponseListener onLogoutResponseListener);
}
