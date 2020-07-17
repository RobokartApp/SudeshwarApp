package com.ark.robokart_robotics.Activities.RegistrationActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.Collect_Recommendation.Collect_RecommendationActivity;
import com.ark.robokart_robotics.Activities.Login.OTPVerficationActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static final int RC_SIGN_IN = 100;

    private CallbackManager callbackManager;

    TextView login_btn;

    Button btn_register;

    ProgressBar login_progress;

    TextView textview_fullname_error, textview_student_number_error, textview_parent_number_error, textview_email_error, textview_username_error, textview_password_error;

    EditText fullname_edt_text, student_number_edt_text, parent_number_edt_text, email_edt_text, username_edt_text, password_edt_text, referal_edt_text;

    LottieAnimationView drawable_anim_fullname, drawable_anim_st_number, drawable_anim_parent_number, drawable_anim_email, drawable_anim_username, drawable_anim_pass;

    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        init();

        listeners();
    }


    //Initialise
    public void init(){
        login_btn = findViewById(R.id.login_btn);
        btn_register = findViewById(R.id.btn_register);
        fullname_edt_text = findViewById(R.id.fullname_edt_text);
        student_number_edt_text = findViewById(R.id.student_number_edt_text);
        parent_number_edt_text = findViewById(R.id.parent_number_edt_text);
        email_edt_text = findViewById(R.id.email_edt_text);
        username_edt_text = findViewById(R.id.username_edt_text);
        password_edt_text = findViewById(R.id.password_edt_text);
        referal_edt_text = findViewById(R.id.referal_edt_text);
        textview_email_error = findViewById(R.id.textview_email_error);
        textview_fullname_error = findViewById(R.id.textview_fullname_error);
        textview_parent_number_error = findViewById(R.id.textview_parent_number_error);
        textview_student_number_error = findViewById(R.id.textview_student_number_error);
        textview_username_error = findViewById(R.id.textview_username_error);
        textview_password_error = findViewById(R.id.textview_password_error);
        drawable_anim_fullname = findViewById(R.id.drawable_anim_fullname);
        drawable_anim_st_number = findViewById(R.id.drawable_anim_st_number);
        drawable_anim_parent_number = findViewById(R.id.drawable_anim_parent_number);
        drawable_anim_email = findViewById(R.id.drawable_anim_email);
        drawable_anim_username = findViewById(R.id.drawable_anim_username);
        drawable_anim_pass = findViewById(R.id.drawable_anim_pass);
        login_progress = findViewById(R.id.login_progress);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);


        try {
            Bundle bundle = getIntent().getExtras();
            fullname_edt_text.setText(bundle.getString("fullname"));
            email_edt_text.setText(bundle.getString("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Event Listeners
    public void listeners(){

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullname_edt_text.getText().toString().trim();
                String email = email_edt_text.getText().toString().trim();
                String st_number = student_number_edt_text.getText().toString().trim();
                String pt_number = parent_number_edt_text.getText().toString().trim();
                String pass = password_edt_text.getText().toString().trim();
                String ref_code = referal_edt_text.getText().toString().trim();
                String username = username_edt_text.getText().toString().trim();

                v.animate().alpha(0.0f);
                login_progress.setVisibility(View.VISIBLE);

                if(fullname.equals("")){
                    textview_fullname_error.setVisibility(View.VISIBLE);
                    drawable_anim_fullname.setAnimation("error.json");
                    drawable_anim_fullname.playAnimation();
                }
                else if(email.equals("")){
                    textview_email_error.setVisibility(View.VISIBLE);
                    drawable_anim_email.setAnimation("error.json");
                    drawable_anim_email.playAnimation();
                }
                else if(st_number.equals("")){
                    textview_student_number_error.setVisibility(View.VISIBLE);
                    drawable_anim_st_number.setAnimation("error.json");
                    drawable_anim_st_number.playAnimation();
                }
                else if(pt_number.equals("")){
                    textview_parent_number_error.setVisibility(View.VISIBLE);
                    drawable_anim_parent_number.setAnimation("error.json");
                    drawable_anim_parent_number.playAnimation();
                }
                else if(pass.equals("")){
                    textview_password_error.setVisibility(View.VISIBLE);
                    drawable_anim_pass.setAnimation("error.json");
                    drawable_anim_pass.playAnimation();
                }

                else if(username.equals("")){
                    textview_username_error.setVisibility(View.VISIBLE);
                    drawable_anim_username.setAnimation("error.json");
                    drawable_anim_username.playAnimation();
                }
                else{
                    textview_fullname_error.setVisibility(View.GONE);
                    textview_username_error.setVisibility(View.GONE);
                    textview_password_error.setVisibility(View.GONE);
                    textview_parent_number_error.setVisibility(View.GONE);
                    textview_student_number_error.setVisibility(View.GONE);
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_username.pauseAnimation();
                    drawable_anim_pass.pauseAnimation();
                    drawable_anim_parent_number.pauseAnimation();
                    drawable_anim_email.pauseAnimation();
                    drawable_anim_st_number.pauseAnimation();
                    drawable_anim_fullname.pauseAnimation();

                    registrationViewModel.registeruser(fullname,email,pass,ref_code,st_number,pt_number,username).observe(RegistrationActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if(s.equals("Email or Mobile Number is already registered")){
                                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                                v.animate().alpha(1.0f);
                                login_progress.setVisibility(View.GONE);
                            }
                            else{
                                v.animate().alpha(1.0f);
                                login_progress.setVisibility(View.GONE);
                                SharedPref sharedPref = new SharedPref();
                                sharedPref.setUserDetails(getApplicationContext(),"",fullname,st_number,email,pt_number,"https://img.icons8.com/officel/2x/user.png",username);

                                sharedPref.setLoginStatus(getApplicationContext(),1);

                                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Collect_RecommendationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

            }
        });

        email_edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(email_edt_text.getText().toString().trim().equals("")){
                    //layout.setError("Please Enter Email ID");
                    textview_email_error.setVisibility(View.VISIBLE);
                    drawable_anim_email.setAnimation("error.json");
                    drawable_anim_email.playAnimation();
                }
                else{
                    if(email_edt_text.getText().toString().trim().matches(emailPattern) ){
                        //layout.setError(null);
                        textview_email_error.setVisibility(View.GONE);

                        drawable_anim_email.setAnimation("check.json");
                        drawable_anim_email.playAnimation();
                    }
                    else{
                        drawable_anim_email.setAnimation("error.json");
                        drawable_anim_email.playAnimation();//layout.setError("Please Enter Valid Email ID");
                        textview_email_error.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        student_number_edt_text .addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = student_number_edt_text.getText().toString();

                if(number.equals("")){
                    //layout.setError("Please Enter Email ID");
                    textview_student_number_error.setVisibility(View.VISIBLE);


                    drawable_anim_st_number.setAnimation("error.json");
                    drawable_anim_st_number.playAnimation();


                }
                else{

                    if(number.length() == 10){
                        //layout.setError(null);
                        textview_student_number_error.setVisibility(View.GONE);

                        drawable_anim_st_number.setAnimation("check.json");
                        drawable_anim_st_number.playAnimation();
                    }
                    else{
                        drawable_anim_st_number.setAnimation("error.json");
                        drawable_anim_st_number.playAnimation();
                        //layout.setError("Please Enter Valid Email ID");
                        textview_student_number_error.setVisibility(View.VISIBLE);
                    }

                }
            }
        });


        parent_number_edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = parent_number_edt_text.getText().toString();

                if(number.equals("")){
                    //layout.setError("Please Enter Email ID");
                    textview_parent_number_error.setVisibility(View.VISIBLE);
                    drawable_anim_parent_number.setAnimation("error.json");
                    drawable_anim_parent_number.playAnimation();


                }
                else{
                    if(number.length() == 10){
                        //layout.setError(null);
                        if(student_number_edt_text.getText().toString().trim().equals(number)){
                            drawable_anim_parent_number.setAnimation("error.json");
                            drawable_anim_parent_number.playAnimation();
                            textview_parent_number_error.setText("Numbers cannot be same");
                            textview_parent_number_error.setVisibility(View.VISIBLE);
                        }
                        else{
                            textview_parent_number_error.setVisibility(View.GONE);
                            drawable_anim_parent_number.setAnimation("check.json");
                            drawable_anim_parent_number.playAnimation();
                        }


                    }

                    else{

                            drawable_anim_parent_number.setAnimation("error.json");
                            drawable_anim_parent_number.playAnimation();
                            //layout.setError("Please Enter Valid Email ID");
                            textview_parent_number_error.setVisibility(View.VISIBLE);


                    }

                }
            }
        });


        username_edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(username_edt_text.getText().toString().trim().equals("")){
                    textview_username_error.setVisibility(View.VISIBLE);
                    drawable_anim_username.setAnimation("error.json");
                    drawable_anim_username.playAnimation();
                }
                else{
                    textview_username_error.setVisibility(View.GONE);
                    drawable_anim_username.setAnimation("check.json");
                    drawable_anim_username.playAnimation();
                }
            }
        });


        password_edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password_edt_text.getText().toString().trim().equals("")){
                    textview_password_error.setVisibility(View.VISIBLE);
                    drawable_anim_pass.setAnimation("error.json");
                    drawable_anim_pass.playAnimation();
                }
                else{
                    textview_password_error.setVisibility(View.GONE);
                    drawable_anim_pass.setAnimation("check.json");
                    drawable_anim_pass.playAnimation();
                }
            }
        });


        fullname_edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(fullname_edt_text.getText().toString().trim().equals("")){
                    textview_password_error.setVisibility(View.VISIBLE);
                    drawable_anim_fullname.setAnimation("error.json");
                    drawable_anim_fullname.playAnimation();
                }
                else{
                    textview_password_error.setVisibility(View.GONE);
                    drawable_anim_fullname.setAnimation("check.json");
                    drawable_anim_fullname.playAnimation();
                }
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
