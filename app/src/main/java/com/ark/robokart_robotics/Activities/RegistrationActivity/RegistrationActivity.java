package com.ark.robokart_robotics.Activities.RegistrationActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    public static final int RC_SIGN_IN = 100;

    private CallbackManager callbackManager;

    TextView login_btn;

    EditText fullname_edt_text, student_number_edt_text, parent_number_edt_text, email_edt_text, username_edt_text, password_edt_text, referal_edt_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

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
    }


    //Event Listeners
    public void listeners(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
