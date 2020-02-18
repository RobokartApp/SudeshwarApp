package com.ark.robokart_robotics.Activities.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ark.robokart_robotics.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        listeners();

    }

    // Initialise UI
    public void init(){
        back_btn = findViewById(R.id.back_btn);
    }

    public void listeners(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
