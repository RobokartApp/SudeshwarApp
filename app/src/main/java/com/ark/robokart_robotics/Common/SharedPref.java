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

    public void setRecommendation(Context context, int status){
        SharedPreferences sharedPreferences =context.getSharedPreferences("recommendation",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("status",status);
        editor.apply();
    }

    public int checkRecommendationStatus(Context context){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("recommendation", Context.MODE_PRIVATE);
        int login_status = sharedPreferences1.getInt("status", 0);
        return login_status;
    }

    public void setStandardSelection(Context context, int status){
        SharedPreferences sharedPreferences =context.getSharedPreferences("standard",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("status",status);
        editor.apply();
    }

    public int checkStandardStatus(Context context){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("standard", Context.MODE_PRIVATE);
        int login_status = sharedPreferences1.getInt("status", 0);
        return login_status;
    }

    public void setUserDetails(Context context, String customer_id, String fullname, String stud_number, String email, String parent_number){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customer_id",customer_id);
        editor.putString("fullname",fullname);
        editor.putString("stud_number",stud_number);
        editor.putString("email",email);
        editor.putString("parent_number",parent_number);
        editor.apply();
    }

    public void setProfileImage(Context context, String image_url) {
        SharedPreferences sharedPreferences =context.getSharedPreferences("URL",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("image_url",image_url);
        editor.apply();
    }
}
