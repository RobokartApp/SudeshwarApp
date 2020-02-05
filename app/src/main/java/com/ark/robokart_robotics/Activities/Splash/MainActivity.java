package com.ark.robokart_robotics.Activities.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.OnBoarding.OnBoardingActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mHandler = new Handler();

        startLoadingData();

    }


    private void startLoadingData() {
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadingDataEnded();
            }
        }, 1000 + random.nextInt(2000));
    }


    private void onLoadingDataEnded() {


//        SharedPreferences sharedPreferences = getSharedPreferences("login_details", Context.MODE_PRIVATE);
//        int status = sharedPreferences.getInt("status", 0);
//
//        if (status==0) {
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        }
//        else {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
//        finish();

        SharedPref sharedPref = new SharedPref();

        int val = sharedPref.getOnboardingStatus(getApplicationContext());

        if(val == 0){
            Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
