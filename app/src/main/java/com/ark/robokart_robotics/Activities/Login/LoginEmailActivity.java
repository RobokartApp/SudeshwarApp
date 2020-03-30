package com.ark.robokart_robotics.Activities.Login;

import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.Collect_Recommendation.Collect_RecommendationActivity;
import com.ark.robokart_robotics.Activities.RegistrationActivity.RegistrationActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.ark.robokart_robotics.Activities.Login.LoginActivity.RC_SIGN_IN;

public class LoginEmailActivity extends AppCompatActivity {

    private static final String TAG = "LoginEmailActivity";

    private EditText email_edt_text, password_edt_text;

    private Button btn_login;

    private CallbackManager callbackManager;

    GoogleSignInClient mGoogleSignInClient;

    SignInButton signInButton;


    private LoginViewModel loginViewModel;

    TextView textview_email_error, textview_password_error;

    LottieAnimationView drawable_anim_email, drawable_anim_pass;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        email_edt_text = findViewById(R.id.email_edt_text);
        password_edt_text = findViewById(R.id.password_edt_text);
        textview_email_error = findViewById(R.id.textview_email_error);
        drawable_anim_email = findViewById(R.id.drawable_anim_email);
        textview_password_error = findViewById(R.id.textview_password_error);
        drawable_anim_pass = findViewById(R.id.drawable_anim_pass);

        btn_login = findViewById(R.id.btn_login);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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


        password_edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textview_password_error.setVisibility(View.GONE);
                drawable_anim_pass.pauseAnimation();
                drawable_anim_pass.setVisibility(View.GONE);
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_edt_text.getText().toString().trim();
                String pass = password_edt_text.getText().toString().trim();

                if(email.equals("") && pass.equals("")){
                    textview_password_error.setVisibility(View.VISIBLE);
                    drawable_anim_pass.setAnimation("error.json");
                    drawable_anim_pass.playAnimation();

                    drawable_anim_email.setAnimation("error.json");
                    drawable_anim_email.playAnimation();//layout.setError("Please Enter Valid Email ID");
                    textview_email_error.setVisibility(View.VISIBLE);
                    textview_email_error.setText("Email cannot be blank");
                }
                else{
                    textview_password_error.setVisibility(View.GONE);
                    drawable_anim_pass.pauseAnimation();

                }

                loginViewModel.loginwithEmail(email,pass).observe(LoginEmailActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        if(s.equals("Login Successfull")){
                            SharedPref sharedPref = new SharedPref();
                            sharedPref.setLoginStatus(LoginEmailActivity.this,1);
                            Intent intent = new Intent(getApplicationContext(), Collect_RecommendationActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //Google SignIn
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //Handle Google Sign In
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();

                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                intent.putExtra("fullname",personName);
                intent.putExtra("email",personEmail);
                intent.putExtra("profilepic",personPhoto);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Welcome "+personName,Toast.LENGTH_SHORT).show();
            }
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }


    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(getApplicationContext(),personName,Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }
}
