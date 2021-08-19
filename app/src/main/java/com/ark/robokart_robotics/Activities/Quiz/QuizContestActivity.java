package com.ark.robokart_robotics.Activities.Quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.robokart_robotics.R;

public class QuizContestActivity extends AppCompatActivity {


    ImageView back_btn;
    TextView launch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_contest);

        back_btn=findViewById(R.id.back_btn);
        launch=findViewById(R.id.launch_contest);

        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}