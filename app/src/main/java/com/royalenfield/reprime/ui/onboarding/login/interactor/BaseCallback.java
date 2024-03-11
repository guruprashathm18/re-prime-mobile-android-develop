package com.royalenfield.reprime.ui.onboarding.login.interactor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.logs.Body;
import com.royalenfield.reprime.models.request.logs.ConnectedLogs;
import com.royalenfield.reprime.models.request.logs.LogRequest;
import com.royalenfield.reprime.models.request.logs.Message;
import com.royalenfield.reprime.models.response.remoteconfig.LoggerBaseUrls;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.view.VerifyTokenView;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T> implements Callback<T> {
    public static void generateLogs(Response response, String url, String msg) {
        if (REUtils.getREKeys()!=null&&!REUtils.getREKeys().getEnableLogging()) {
            return;
        }
      //  LogRequest req = new LogRequest();
         com.royalenfield.reprime.models.request.logs.Log log = new com.royalenfield.reprime.models.request.logs.Log();

        log.setEnvironment("Android");
log.setIp(REUtils.getIP(REApplication.getAppContext()));
        log.setAppId(getEnvironment());
        log.setTimestamp(dateFormater());
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
            log.setUser(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        else
            log.setUser("Guest");

        Message message = new Message();

        StringBuilder type =new StringBuilder("");
        Body body = new Body();
        type.append( "/"+BuildConfig.VERSION_NAME+"/"+BuildConfig.VERSION_CODE+"/");
        if (response != null) {
           // type = "Misc";
            String baseurl = response.raw().request().url().toString();
         String   type1 = baseurl.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "");
           type1=type1.split("\\?")[0];
            if (type1.endsWith("/")) {
                type.append(type1.substring(0, type1.length() - 1));
            }
            else
                type.append(type1);
            body.setResponse(new Gson().toJson(response.body()) + "|"+response.code()+":"+response.message());
            body.setUrl(response.raw().request().url() + "");
            body.setRequest(bodyToString(response.raw().request().body()));
            try {
                body.setTimeDifference((response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis()) / 1000 + " Seconds");
            }
            catch (Exception e){
                RELog.e(e);
            }
            message.setBody(body);
            log.setMessageType("error");
            log.setClassname(response.raw().request().url() + "");
            message.setTitle("API error");
            log.setTitle("API error");
              type.append( "/Android");
            log.setJwt(response.raw().request().header("Authorization"));

        } else {

            if (url.contains("https://")) {
                type.append(url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", ""));
                type.append("Firebase/" + type + "Android");
            } else {
                type.append("Firestore/" + url.substring(url.lastIndexOf("/") + 1));
                type.append("/Android");
            }
            log.setMessageType("error");
            message.setTitle("Firebase error");
            body.setResponse(msg);
            message.setBody(body);
            log.setTitle("Firebase error");
            body.setUrl(url);
            log.setClassname(url + "");

        }
       // log.setDeviceInfo(Build.MANUFACTURER+"-"+Build.BRAND+" ("+Build.VERSION.SDK_INT+" "+Build.VERSION.RELEASE+")");
        log.setApp_version(BuildConfig.VERSION_NAME+"("+BuildConfig.VERSION_CODE+")");
        log.setMessage(message);
        log.setBrand(Build.MANUFACTURER+" - "+Build.BRAND);
        log.setModel(Build.MODEL);
        log.setOs(Build.VERSION.SDK_INT+" - "+Build.VERSION.RELEASE);
        int[] speed=getNetworkSpeed();
        if(speed!=null&&speed.length>1)
            log.setNetworkStrength("Download Speed(KBPS)- "+speed[0]+" Upload Speed(KBPS)- "+speed[1]);
        Log.e("RES", new Gson().toJson(log));
        Log.e("TYPE>>>>>>>>>>>>>>>>>", type.toString());
        uploadLogs(log, type.toString(),log.getAppId());
    }




    public void generateFailureLogs(Call response,Throwable throwable,long timediff) {

        if (REUtils.getREKeys()!=null&&!REUtils.getREKeys().getEnableLogging()) {
            return;
        }
        //  LogRequest req = new LogRequest();
        com.royalenfield.reprime.models.request.logs.Log log = new com.royalenfield.reprime.models.request.logs.Log();

        log.setEnvironment("Android");
		log.setIp(REUtils.getIP(REApplication.getAppContext()));
        log.setAppId(getEnvironment());
        log.setTimestamp(dateFormater());
		if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
			log.setJwt(REApplication.getInstance().getUserTokenDetails());
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
            log.setUser(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        else
            log.setUser("Guest");

        Message message = new Message();

        StringBuilder type =new StringBuilder("");
        Body body = new Body();
        type.append( "/"+BuildConfig.VERSION_NAME+"/"+BuildConfig.VERSION_CODE+"/");
        if (response != null) {
            // type = "Misc";
            type.append("failure/");
            String baseurl = response.request().url().toString();

            String   type1 = baseurl.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "");
            type1=type1.split("\\?")[0];
            if (type1.endsWith("/")) {
                type.append(type1.substring(0, type1.length() - 1));
            }
            else
                type.append(type1);
            body.setResponse(throwable.getMessage());
            body.setUrl(response.request().url() + "");
            body.setRequest(bodyToString(response.request().body()));
            try {
                body.setTimeDifference(timediff + " Seconds");
            }
            catch (Exception e){
                RELog.e(e);
            }
            message.setBody(body);
            log.setMessageType("error");
            log.setClassname(response.request().url() + "");
            message.setTitle(throwable.getMessage());
            log.setTitle("API error");
            type.append( "/Android");
        }
        log.setApp_version(BuildConfig.VERSION_NAME+"("+BuildConfig.VERSION_CODE+")");
        log.setMessage(message);
      //  log.setDeviceInfo(Build.MANUFACTURER+"-"+Build.BRAND+" ("+Build.VERSION.SDK_INT+" "+Build.VERSION.RELEASE+")");
        log.setBrand(Build.MANUFACTURER+" - "+Build.BRAND);
        log.setModel(Build.MODEL);
        log.setOs("SDK-"+Build.VERSION.SDK_INT+" "+"OS-"+Build.VERSION.RELEASE);
        int[] speed=getNetworkSpeed();
        if(speed!=null&&speed.length>1)
            log.setNetworkStrength("Download Speed(KBPS)- "+speed[0]+" Upload Speed(KBPS)- "+speed[1]);
        Log.e("RES", new Gson().toJson(log));
        Log.e("TYPE>>>>>>>>>>>>>>>>>", type.toString());
        uploadLogs(log, type.toString(),log.getAppId());
    }

public static int[] getNetworkSpeed(){
        try {
            ConnectivityManager cm = (ConnectivityManager) REApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            int downSpeed = nc.getLinkDownstreamBandwidthKbps();
            int upSpeed = nc.getLinkUpstreamBandwidthKbps();
            Log.e("SPEED", downSpeed + " " + upSpeed);
            return new int[]{downSpeed, upSpeed};
        }
        catch (Exception e){
            return null;
        }
}
    public static String dateFormater() {
        Calendar calendar = Calendar.getInstance();
        //   TimeZone tz = TimeZone.getDefault();
        // calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zzz", Locale.getDefault());
        java.util.Date currenTimeZone = new java.util.Date((long) calendar.getTimeInMillis());
        return sdf.format(currenTimeZone);
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static void uploadLogs(com.royalenfield.reprime.models.request.logs.Log req, String header,String appId) {
        LoggerBaseUrls loggerBaseUrls = REUtils.getLoggerappbaseURLs();
        HashMap map = new HashMap();
        map.put(loggerBaseUrls.getQueryParamKey(), loggerBaseUrls.getQueryParamValue());
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteLoggerAPI()
                .uploadLog(header,appId, req, map)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                    }
                });
    }


	public static void uploadConnectedLogs(ConnectedLogs logs) {
		REApplication
				.getInstance()
				.getREWebsiteApiInstance()
				.getConnectedLoggerAPI()
				.UploadConnectedLog( logs)
				.enqueue(new Callback<String>() {
					@Override
					public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
						if (response.isSuccessful() && response.body() != null) {
						}
					}

					@Override
					public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

					}
				});
	}


	@Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.e("Base", "Response");
        if (!response.isSuccessful())
            generateLogs(response, null, null);

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("BAse", "FAILURE");
        generateFailureLogs(call,t,-1);

    }

    public static String getEnvironment(){
        String env="reprime";
        if(BuildConfig.FLAVOR.equals("dev"))
           env="reprime-sit";
        else if(BuildConfig.FLAVOR.equals("uat"))
            env="reprime-uat";
        else
            env="reprime";

        return env;
    }
}
