package com.ark.robokart_robotics.Activities.RegistrationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.terms.TermsActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;
/*
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
*/
public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static final int RC_SIGN_IN = 100;

   // private CallbackManager callbackManager;

    TextView login_btn;

    Button btn_register;
    LinearLayout terms;

    ProgressBar login_progress;

    TextView textview_fullname_error,textview_grade_error,textview_havePc_error, textview_student_number_error, textview_parent_number_error, textview_email_error, textview_username_error, textview_password_error;

    EditText fullname_edt_text, student_number_edt_text, parent_number_edt_text, email_edt_text, username_edt_text, password_edt_text, referal_edt_text;

    LottieAnimationView drawable_anim_fullname,drawable_anim_grade,drawable_anim_pc, drawable_anim_st_number, drawable_anim_parent_number, drawable_anim_email, drawable_anim_username, drawable_anim_pass;

    private RegistrationViewModel registrationViewModel;
    Spinner gradeSpin;
    RadioGroup pcGroup;
    String isPc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        init();

        listeners();
    }


    //Initialise
    public void init(){
        textview_grade_error=findViewById(R.id.textview_grade_error);
        textview_havePc_error=findViewById(R.id.have_pc_error);
        pcGroup=findViewById(R.id.pc_group);
        gradeSpin=findViewById(R.id.grade_spinner);
        terms=findViewById(R.id.terms_and_conditions);
        login_btn = findViewById(R.id.login_btn);
        btn_register = findViewById(R.id.btn_register);
        fullname_edt_text = findViewById(R.id.fullname_edt_text);
        student_number_edt_text = findViewById(R.id.student_number_edt_text);
        parent_number_edt_text = findViewById(R.id.parent_number_edt_text);
        email_edt_text = findViewById(R.id.email_edt_text);
        username_edt_text = findViewById(R.id.username_edt_text);
       // password_edt_text = findViewById(R.id.password_edt_text);
//        referal_edt_text = findViewById(R.id.referal_edt_text);
        textview_email_error = findViewById(R.id.textview_email_error);
        textview_fullname_error = findViewById(R.id.textview_fullname_error);
//        textview_parent_number_error = findViewById(R.id.textview_parent_number_error);
        textview_student_number_error = findViewById(R.id.textview_student_number_error);
        textview_username_error = findViewById(R.id.textview_username_error);
//        textview_password_error = findViewById(R.id.textview_password_error);
        drawable_anim_fullname = findViewById(R.id.drawable_anim_fullname);
        drawable_anim_st_number = findViewById(R.id.drawable_anim_st_number);
        drawable_anim_pc = findViewById(R.id.drawable_anim_pc);
        drawable_anim_email = findViewById(R.id.drawable_anim_email);
        drawable_anim_username = findViewById(R.id.drawable_anim_username);
        drawable_anim_grade = findViewById(R.id.drawable_anim_grade);
        login_progress = findViewById(R.id.login_progress);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);


        try {
            Bundle bundle = getIntent().getExtras();
            if(  bundle!=null) {
                fullname_edt_text.setText(bundle.getString("fullname"));
                email_edt_text.setText(bundle.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Event Listeners
    public void listeners(){

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullname_edt_text.getText().toString().trim();
                String email = email_edt_text.getText().toString().trim();
                String st_number = student_number_edt_text.getText().toString().trim();
//                String pt_number = parent_number_edt_text.getText().toString().trim();
//                String pass = password_edt_text.getText().toString().trim();
//                String ref_code = referal_edt_text.getText().toString().trim();
                String username = username_edt_text.getText().toString().trim();
                String grade=gradeSpin.getSelectedItem().toString();
                int havePc = pcGroup.getCheckedRadioButtonId();

                if(fullname.equals("")){
                    fullname_edt_text.requestFocus();
                    textview_fullname_error.setVisibility(View.VISIBLE);
                    drawable_anim_fullname.setAnimation("error.json");
                    drawable_anim_fullname.playAnimation();
                }
                else if(username.equals("")){
                    username_edt_text.requestFocus();
                    textview_username_error.setVisibility(View.VISIBLE);
                    drawable_anim_username.setAnimation("error.json");
                    drawable_anim_username.playAnimation();
                }
                else if(st_number.equals("")){
                    student_number_edt_text.requestFocus();
                    textview_student_number_error.setVisibility(View.VISIBLE);
                    drawable_anim_st_number.setAnimation("error.json");
                    drawable_anim_st_number.playAnimation();
                }
                else if(email.equals("")){
                    email_edt_text.requestFocus();
                    textview_email_error.setVisibility(View.VISIBLE);
                    drawable_anim_email.setAnimation("error.json");
                    drawable_anim_email.playAnimation();
                }
                else if(grade.equals("Choose Grade")){
                    textview_grade_error.setVisibility(View.VISIBLE);
                    drawable_anim_grade.setAnimation("error.json");
                    drawable_anim_grade.playAnimation();
                }
                else if(havePc==-1){
                    textview_havePc_error.setVisibility(View.VISIBLE);
                    drawable_anim_pc.setAnimation("error.json");
                    drawable_anim_pc.playAnimation();
                }
                else{
                    v.animate().alpha(0.0f);
                    login_progress.setVisibility(View.VISIBLE);
                    textview_fullname_error.setVisibility(View.GONE);
                    textview_username_error.setVisibility(View.GONE);
                    textview_grade_error.setVisibility(View.GONE);
                    textview_havePc_error.setVisibility(View.GONE);
                    textview_student_number_error.setVisibility(View.GONE);
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_username.pauseAnimation();
                    drawable_anim_grade.pauseAnimation();
                    drawable_anim_pc.pauseAnimation();
                    drawable_anim_email.pauseAnimation();
                    drawable_anim_st_number.pauseAnimation();
                    drawable_anim_fullname.pauseAnimation();

                    registrationViewModel.registeruser(fullname,username,st_number,email,grade,isPc).observe(RegistrationActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.d("reg user modal",s);
                            if(s.equals("Email or Mobile Number is already registered")){
                                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                                v.animate().alpha(1.0f);
                                login_progress.setVisibility(View.GONE);
                            }
                            else{
                                v.animate().alpha(1.0f);
                                login_progress.setVisibility(View.GONE);
                                SharedPref sharedPref = new SharedPref();
                                sharedPref.setUserDetails(getApplicationContext(),s,fullname,st_number,email,"","https://img.icons8.com/officel/2x/user.png",username);

                                sharedPref.setLoginStatus(getApplicationContext(),1);

                                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

        gradeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // your code here
        if(gradeSpin.getSelectedItem().toString().equals("Choose Grade")){

        }else{
            textview_grade_error.setVisibility(View.GONE);

            drawable_anim_grade.setAnimation("check.json");
            drawable_anim_grade.playAnimation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
    }

});

        pcGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        textview_havePc_error.setVisibility(View.GONE);

        drawable_anim_pc.setAnimation("check.json");
        drawable_anim_pc.playAnimation();
        //Toast.makeText(RegistrationActivity.this, "rad id: "+i, Toast.LENGTH_SHORT).show();
        RadioButton rb = findViewById(i);

        switch (rb.getText().toString()) {
            case "YES": // COD
                    isPc="Yes";
                break;
            case "NO": // UPI
                    isPc="No";
                break;
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

/*
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
*/

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
                    textview_fullname_error.setVisibility(View.VISIBLE);
                    drawable_anim_fullname.setAnimation("error.json");
                    drawable_anim_fullname.playAnimation();
                }
                else{
                    textview_fullname_error.setVisibility(View.GONE);
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
