package com.ark.robokart_robotics.Activities.ChooseCategory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ark.robokart_robotics.Activities.NewQuiz.NewQuizActivity;
import com.ark.robokart_robotics.R;

public class ChooseCategory extends AppCompatActivity {

    RelativeLayout rlMaker, rlFreeCourse, rlAtlCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        init();

        listeners();

    }

    public void init(){
        rlMaker = findViewById(R.id.rlMaker);
        rlFreeCourse = findViewById(R.id.rlFreeCourse);
        rlAtlCourses = findViewById(R.id.rlAtlCourses);
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
    }
}