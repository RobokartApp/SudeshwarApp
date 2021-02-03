package com.ark.robokart_robotics.Activities.ChooseCategory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ark.robokart_robotics.Activities.NewQuiz.NewQuizActivity;
import com.ark.robokart_robotics.R;

import java.util.Calendar;
import java.util.Date;

public class ChooseCategory extends AppCompatActivity {

    RelativeLayout rlMaker, rlFreeCourse, rlAtlCourses;
    private TextView tvName,tvGood;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        init();

        listeners();

    }

    public void init(){
        tvName = findViewById(R.id.tvName);
        back_btn = findViewById(R.id.back_btn);
        tvGood = findViewById(R.id.tvGood);
        rlMaker = findViewById(R.id.rlMaker);
        rlFreeCourse = findViewById(R.id.rlFreeCourse);
        rlAtlCourses = findViewById(R.id.rlAtlCourses);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            String fullname = sharedPreferences.getString("fullname","-");

            String lastName = "";
            String firstName= "";
            if(fullname.split("\\w+").length>1){

                lastName = fullname.substring(fullname.lastIndexOf(" ")+1);
                firstName = fullname.substring(0, fullname.lastIndexOf(' '));
            }
            else{
                firstName = fullname;
            }

            tvName.setText(firstName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        //Set greeting
        String greeting = null;
        if(hour>=6 && hour<12){
            greeting = "Good Morning,";
        } else if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon,";
        } else if(hour >= 17 && hour < 24){
            greeting = "Good Evening,";
        }
        else if(hour >= 0 && hour < 4){
            greeting = "Good Night,";
        }

        tvGood.setText(greeting);
    }

    public void listeners(){

        rlMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), NewQuizActivity.class);
                 startActivity(intent);
            }
        });

        rlFreeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlAtlCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}