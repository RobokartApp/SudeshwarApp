package com.ark.robokart_robotics.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.OnBoarding.OnBoardingActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

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
