package com.ark.robokart_robotics.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public void setOnboardingStatus(Context context, int status){
        SharedPreferences sharedPreferences =context.getSharedPreferences("onboarding",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("onboarding_status",status);
        editor.apply();
    }


    public int getOnboardingStatus(Context context){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        int value = sharedPreferences1.getInt("onboarding_status", 0);
        return value;
    }

    public int checkLoginStatus(Context context){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        int login_status = sharedPreferences1.getInt("status", 0);
        return login_status;
    }

    public void setLoginStatus(Context context, int status) {
        SharedPreferences sharedPreferences =context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("status",status);
        editor.apply();
    }
}
