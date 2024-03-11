package com.royalenfield.firebase.fcm.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neolane.android.v1.Neolane;
import com.neolane.android.v1.NeolaneAsyncRunner;
import com.neolane.android.v1.NeolaneException;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.splash.activity.MainSplashScreenActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;
import java.util.Map;

import com.royalenfield.reprime.utils.RELog;
import com.salesforce.marketingcloud.messages.push.PushMessageManager;
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk;

import static com.royalenfield.reprime.utils.REConstants.NOTIF_DATA_PARAMS;
import static com.royalenfield.reprime.utils.REConstants.PUSH_ACM_TRACKING_MID_MESSAGE;
import static com.royalenfield.reprime.utils.REConstants.PUSH_REMOTE_MESSAGE;


public class REFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = REFirebaseMessagingService.class.getSimpleName();
    private String title = "Test Notification";
    private String body = "Test Notification";
    private String type = "type";
    JsonObject aJsonObjectAlert;
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e(TAG, "Refreshed token: " + token);
        REUtils.saveFcmRegistrationTokenIntoPref(token);
        REUtils.setFCMTokenSent(false);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "onMessageReceived From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(RemoteMessage messageBody) {
        try {
            Intent intent = null;
            Map<String, String> payload = messageBody.getData();

          //  Object[] keys = payload.keySet().toArray();
             String alertStr = payload.get("alert");
            String jsonDataStr = payload.get(NOTIF_DATA_PARAMS);
			String sId= payload.get("_sid");
            JsonElement jelem = new Gson().fromJson(alertStr, JsonElement.class);
            aJsonObjectAlert= jelem.getAsJsonObject();

            //If app is background or foreground

                if( REUtils.isUserLoggedIn()){
                    if(REUserModelStore.getInstance().getUserId()==null||REUserModelStore.getInstance().getUserId().equalsIgnoreCase("")) {
                        intent = new Intent(this, MainSplashScreenActivity.class);
                } else {
                    intent = new Intent(this, REHomeActivity.class);
                }
            } else {
                intent = new Intent(this, MainSplashScreenActivity.class);
            }

			int requestID = (int) System.currentTimeMillis();
            intent.putExtra(PUSH_REMOTE_MESSAGE, jsonDataStr);
            handlePushNotificationPayload(aJsonObjectAlert);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				PendingIntent intent1;
				if(sId!=null&&sId.equals("SFMC"))
				intent1=    com.salesforce.marketingcloud.notifications.NotificationManager.redirectIntentForAnalytics(getApplicationContext(), PendingIntent.getActivity(getApplicationContext(),requestID, intent, PendingIntent.FLAG_IMMUTABLE),messageBody,true);
				else
				intent1=PendingIntent.getActivity(this, requestID/*request code*/, intent, PendingIntent.FLAG_IMMUTABLE);
				if(aJsonObjectAlert.isJsonObject()&&aJsonObjectAlert.has("image")&&!aJsonObjectAlert.get(REConstants.NOTIF_DATA_IMAGE).getAsString().isEmpty()){
                   String image = aJsonObjectAlert.get(REConstants.NOTIF_DATA_IMAGE).getAsString();
				     setNotificationBuilder(  intent1,true,image);
                }
                else
                setNotificationBuilder(intent1,false,null);
            }

        } catch (Exception e) {
        }
    }

    /**
     * Notification builder
     *
     * @param pendingIntent : PendingIntent
     */
    private void setNotificationBuilder(PendingIntent pendingIntent,boolean isImageNotification,String image) {
        //Channel ID for the notification
        String NOTIFICATION_CHANNEL_ID = getString(R.string.re_notification_channel_id);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "REPrime Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("REPrime channel for the notification");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder  notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_noti_test)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        if(isImageNotification)
        {
            Glide.with(this)
                    .asBitmap().load(image)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            notificationBuilder .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
                            notificationManager.notify((int)System.currentTimeMillis(), notificationBuilder.build());
                            return false;
                        }


                              }
                    ).submit();
        }
        else {
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    /**
     * Handle the payload for generic modules based on App screen Indexing.
     *
     * @param aJsonObject
     */
    private void handlePushNotificationPayload(JsonObject aJsonObject) {
        try {
            title = aJsonObject.get(REConstants.NOTIF_DATA_TITLE).getAsString();
            body = aJsonObject.get(REConstants.NOTIF_DATA_BODY).getAsString();
            RELog.e(REFirebaseMessagingService.class.getSimpleName(),"Generic:PushNotificationPayload: Title:" + title + ", Body:" + body);
        } catch (Exception e) {
            RELog.e(e);
        }
    }
}