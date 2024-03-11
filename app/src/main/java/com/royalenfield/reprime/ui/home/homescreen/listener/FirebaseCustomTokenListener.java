package com.royalenfield.reprime.ui.home.homescreen.listener;

public interface FirebaseCustomTokenListener {
    public void onFirebaseCustomTokenSuccess(String token,String reqId);
    public void onFirebaseCustomFailure(String error);
	public void onFirebaseAuthSuccess();
}
