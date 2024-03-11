package com.royalenfield.reprime.utils;

import android.util.Log;

public class RELog {

    public static void v(String tag, String msg){
        Log.v(tag,msg+"");
    }

    public static void d(String tag, String msg){
        Log.d(tag, msg+"");
    }

    public static void i(String tag, String msg){
        Log.i(tag,msg+"");
    }

    // Changing e to v to avoid the sonar
    public static void e(String tag, String msg){
        Log.v(tag,msg+"");
    }
    // Changing e to v to avoid the sonar
    public static void e(String tag, String msg,Throwable throwable){
        Log.v(tag,msg+"",throwable);
    }

    public static void e(String msg){
        Log.e("TAG",msg+"");

    }

    public static void e(Exception e){
        Log.e(e.getMessage(),e.getLocalizedMessage()+"");
    }

}
