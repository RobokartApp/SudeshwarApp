package com.ark.robokart_robotics.Activities.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.OnBoarding.OnBoardingActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    LottieAnimationView logo;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);



        logo.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                onLoadingDataEnded();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mHandler = new Handler();

//        startLoadingData();

    }


    private void startLoadingData() {
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadingDataEnded();
            }
        }, 1000 + random.nextInt(10000));
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

        int status_val = sharedPref.checkLoginStatus(getApplicationContext());


        int status_standard = sharedPref.checkStandardStatus(getApplicationContext());

        if(status_val == 1){
            if(val == 0){
                Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            if(val == 0){
                Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
