package com.ark.robokart_robotics.Activities.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.ark.robokart_robotics.Activities.RegistrationActivity.RegistrationActivity;
import com.ark.robokart_robotics.Common.Validation;
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
import com.subsub.library.BeautyButton;
import com.subsub.library.BeautyLayout;

import org.json.JSONObject;

import java.util.Arrays;

import carbon.widget.Button;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final int RC_SIGN_IN = 100;

    Button btn_login, btn_email_login;

    LoginButton btn_fb;

    private CallbackManager callbackManager;

    GoogleSignInClient mGoogleSignInClient;

    SignInButton signInButton;

    TextView sign_up_btn, textview_number_error;

    EditText etPhoneNum;

    Validation validation;

    LottieAnimationView animationView;

    public LoginViewModel loginViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        init();

        listeners();



    }



    //Initialise
    public void init(){

        textview_number_error = findViewById(R.id.textview_number_error);

        etPhoneNum = findViewById(R.id.etPhoneNum);

        btn_login = findViewById(R.id.btn_login);

        btn_email_login = findViewById(R.id.btn_email_login);

        btn_fb = findViewById(R.id.login_button);

        animationView = findViewById(R.id.drawable_anim);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        sign_up_btn = findViewById(R.id.sign_up_btn);

        callbackManager = CallbackManager.Factory.create();
        btn_fb.setPermissions(Arrays.asList("email","public_profile"));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        validation = new Validation();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

    }


    //Event Listeners
    public void listeners(){

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etPhoneNum.getText().length() > 9 && textview_number_error.getVisibility() == View.GONE){

                    String number = etPhoneNum.getText().toString().trim();

                    loginViewModel.requestotp(number).observe(LoginActivity.this, s -> {
                        if(s.equals("OTP has been sent to your mobile number"))
                        {
                            Intent intent = new Intent(getApplicationContext(),OTPVerficationActivity.class);
                            intent.putExtra("phone_number",number);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    textview_number_error.setVisibility(View.VISIBLE);
                    animationView.setAnimation("error.json");
                    animationView.playAnimation();
                }
            }
        });


        btn_email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });




        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = etPhoneNum.getText().toString();

                if(number.equals("")){
                    //layout.setError("Please Enter Email ID");
                    textview_number_error.setVisibility(View.VISIBLE);


                    animationView.setAnimation("error.json");
                    animationView.playAnimation();


                }
                else{

                    if(number.length() == 10){
                        //layout.setError(null);
                        textview_number_error.setVisibility(View.GONE);

                        animationView.setAnimation("check.json");
                        animationView.playAnimation();
                    }
                    else{
                        animationView.setAnimation("error.json");
                        animationView.playAnimation();
                        //layout.setError("Please Enter Valid Email ID");
                        textview_number_error.setVisibility(View.VISIBLE);
                    }

                }
            }
        });




        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    AccessTokenTracker tracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {


            if(currentAccessToken == null){
                Toast.makeText(getApplicationContext(),"You have been logged out",Toast.LENGTH_SHORT).show();
            }
            else{
                loadUserProfile(currentAccessToken);
            }

        }
    };


    //Facebook loaduserDetails
    private void loadUserProfile(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{

                            String first_name = object.getString("name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");

                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Toast.makeText(getApplicationContext(),first_name,Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){}


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
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

                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                intent.putExtra("fullname",personName);
                intent.putExtra("email",personEmail);
                intent.putExtra("profilepic",personPhoto);
                startActivity(intent);

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


        }
        super.onStart();
    }
}
