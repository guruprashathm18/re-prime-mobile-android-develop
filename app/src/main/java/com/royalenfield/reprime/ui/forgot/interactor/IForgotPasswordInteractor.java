package com.royalenfield.reprime.ui.forgot.interactor;

import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.forgot.presenter.ForgotPasswordPresenter;

/**
 * @author BOP1KOR on 12/19/2018.
 * <p>
 * An Interactor helps models cross application boundaries such as networks or serialization
 * This IForgotPasswordInteractor{@link ForgotPasswordInteractor} knows nothing about a UI or the {@link ForgotPasswordPresenter}
 */

public interface IForgotPasswordInteractor {

    /**
     * Api call to generate the OTP using user email id or phone number.
     *
     * @param userId                    user entered registered id can be phone number or email id.
     * @param onSendOtpResponseListener To communicate to presenter.
     */
    void sendOtp(String userId, OnSendOtpResponseListener onSendOtpResponseListener);
}
