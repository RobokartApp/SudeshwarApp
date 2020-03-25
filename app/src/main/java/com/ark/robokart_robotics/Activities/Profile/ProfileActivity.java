package com.ark.robokart_robotics.Activities.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.R;

import carbon.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    ImageView back_btn;

    private EditText first_name_edt, last_name_edt, email_edt, phone_edt;

    private Button save_btn;

    private LottieAnimationView drawable_anim_fname, drawable_anim_lname, drawable_anim_email, drawable_anim_number;

    private TextView textview_fname_error, textview_lname_error, textview_email_error, textview_phone_error;

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
        first_name_edt = findViewById(R.id.first_name_edt);
        last_name_edt = findViewById(R.id.last_name_edt);
        email_edt = findViewById(R.id.email_edt);
        phone_edt = findViewById(R.id.phone_edt);
        save_btn = findViewById(R.id.save_btn);
        drawable_anim_fname = findViewById(R.id.drawable_anim_fname);
        drawable_anim_lname = findViewById(R.id.drawable_anim_lname);
        drawable_anim_email = findViewById(R.id.drawable_anim_email);
        drawable_anim_number = findViewById(R.id.drawable_anim_number);
        textview_fname_error = findViewById(R.id.textview_fname_error);
        textview_lname_error = findViewById(R.id.textview_lname_error);
        textview_email_error = findViewById(R.id.textview_email_error);
        textview_phone_error = findViewById(R.id.textview_phone_error);
    }

    public void listeners(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        first_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fname = first_name_edt.getText().toString().trim();
                if(fname.equals("")){
                    textview_fname_error.setVisibility(View.VISIBLE);
                    textview_phone_error.setVisibility(View.GONE);
                    textview_lname_error.setVisibility(View.GONE);
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_fname.setAnimation("error.json");
                    drawable_anim_fname.playAnimation();
                }
                else{
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_fname.setVisibility(View.GONE);
                }
            }
        });

        last_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String lname = last_name_edt.getText().toString().trim();
                if(lname.equals("")){
                    textview_lname_error.setVisibility(View.VISIBLE);
                    textview_phone_error.setVisibility(View.GONE);
                    textview_fname_error.setVisibility(View.GONE);
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_lname.setAnimation("error.json");
                    drawable_anim_lname.playAnimation();
                }
                else{
                    textview_lname_error.setVisibility(View.GONE);
                    drawable_anim_lname.pauseAnimation();
                }
            }
        });

        email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = email_edt.getText().toString().trim();
                if(email.equals("")){
                    textview_email_error.setVisibility(View.VISIBLE);
                    textview_lname_error.setVisibility(View.GONE);
                    textview_phone_error.setVisibility(View.GONE);
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_email.setAnimation("error.json");
                    drawable_anim_email.playAnimation();
                }
                else{
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_email.setVisibility(View.GONE);
                }
            }
        });

        phone_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = phone_edt.getText().toString().trim();
                if(phone.equals("")){
                    textview_email_error.setVisibility(View.GONE);
                    textview_lname_error.setVisibility(View.GONE);
                    textview_phone_error.setVisibility(View.VISIBLE);
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_number.setAnimation("error.json");
                    drawable_anim_number.playAnimation();
                }
                else{
                    textview_phone_error.setVisibility(View.GONE);
                    drawable_anim_number.setVisibility(View.GONE);
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textview_fname_error.getVisibility() == View.GONE && textview_lname_error.getVisibility() == View.GONE && textview_email_error.getVisibility() == View.GONE
                  && textview_phone_error.getVisibility() == View.GONE){
                    finish();
                }
            }
        });
    }
}
