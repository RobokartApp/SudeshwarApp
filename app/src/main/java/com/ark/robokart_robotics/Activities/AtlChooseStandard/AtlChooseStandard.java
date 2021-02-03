package com.ark.robokart_robotics.Activities.AtlChooseStandard;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.DBHelper;
import com.ark.robokart_robotics.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AtlChooseStandard extends AppCompatActivity {

    public CardView fifthstdcard,sixstd,sevenstd,eightstd,ninestd,tenstd,elevenstd,twelvestd;
    private TextView tvName,tvGood;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atl_choose_standard);

        init();

        listeners();

    }

    public void init(){
        fifthstdcard = findViewById(R.id.fifthstdcard);
        sixstd=findViewById(R.id.sixthstd);
        sevenstd=findViewById(R.id.sevenstd);
        eightstd=findViewById(R.id.eightstd);
        ninestd=findViewById(R.id.ninestd);
        tenstd=findViewById(R.id.tenstd);
        elevenstd=findViewById(R.id.elevenstd);
        twelvestd=findViewById(R.id.twelvestd);

        tvName = findViewById(R.id.tvName);
        back_btn = findViewById(R.id.back_btn);
        tvGood = findViewById(R.id.tvGood);
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
public static String std="";
    public void listeners(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fifthstdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="5";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        sixstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="6";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        sevenstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="7";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        eightstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="8";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        ninestd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="9";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        tenstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="10";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        elevenstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="11";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
        twelvestd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                std="12";
                Intent intent = new Intent(getApplicationContext(), AtlChooseLevel.class);
                startActivity(intent);
            }
        });
    }
}