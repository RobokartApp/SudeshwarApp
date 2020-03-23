package com.ark.robokart_robotics.Activities.RegistrationActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

    TextView textview_fullname_error, textview_student_number_error, textview_parent_number_error, textview_email_error, textview_username_error, textview_password_error;

    EditText fullname_edt_text, student_number_edt_text, parent_number_edt_text, email_edt_text, username_edt_text, password_edt_text, referal_edt_text;

    LottieAnimationView animationView;

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
    }


    //Event Listeners
    public void listeners(){

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
                    animationView.setAnimation("error.json");
                    animationView.playAnimation();
                }
                else{
                    if(email_edt_text.getText().toString().trim().matches(emailPattern) ){
                        //layout.setError(null);
                        textview_email_error.setVisibility(View.GONE);

                        animationView.setAnimation("check.json");
                        animationView.playAnimation();
                    }
                    else{
                        animationView.setAnimation("error.json");
                        animationView.playAnimation();//layout.setError("Please Enter Valid Email ID");
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


                    animationView.setAnimation("error.json");
                    animationView.playAnimation();


                }
                else{

                    if(number.length() == 10){
                        //layout.setError(null);
                        textview_student_number_error.setVisibility(View.GONE);

                        animationView.setAnimation("check.json");
                        animationView.playAnimation();
                    }
                    else{
                        animationView.setAnimation("error.json");
                        animationView.playAnimation();
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


                    animationView.setAnimation("error.json");
                    animationView.playAnimation();


                }
                else{

                    if(number.length() == 10){
                        //layout.setError(null);
                        textview_parent_number_error.setVisibility(View.GONE);

                        animationView.setAnimation("check.json");
                        animationView.playAnimation();
                    }
                    else{
                        animationView.setAnimation("error.json");
                        animationView.playAnimation();
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
                    animationView.setAnimation("error.json");
                    animationView.playAnimation();
                }
                else{
                    textview_username_error.setVisibility(View.GONE);
                    animationView.setAnimation("check.json");
                    animationView.playAnimation();
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
                    animationView.setAnimation("error.json");
                    animationView.playAnimation();
                }
                else{
                    textview_password_error.setVisibility(View.GONE);
                    animationView.setAnimation("check.json");
                    animationView.playAnimation();
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
