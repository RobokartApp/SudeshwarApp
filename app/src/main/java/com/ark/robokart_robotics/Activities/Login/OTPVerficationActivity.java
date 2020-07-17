package com.ark.robokart_robotics.Activities.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ark.robokart_robotics.Activities.Collect_Recommendation.Collect_RecommendationActivity;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;
import com.ark.robokart_robotics.Reciever.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.subsub.library.BeautyButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import carbon.widget.Button;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OTPVerficationActivity extends AppCompatActivity  {

    private static final int REQ_USER_CONSENT = 200;

    private OtpTextView otpTextView;

    Button btnVerify;

    SmsBroadcastReceiver smsBroadcastReceiver;

    public OTPViewModel otpViewModel;

    String phone_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverfication);


        Bundle bundle = getIntent().getExtras();
        phone_number = bundle.getString("phone_number");

        init();

        listeners();
    }


    public void init(){

        otpTextView = findViewById(R.id.otp_view);

        btnVerify=findViewById(R.id.btnVerify);

        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class);


        startSmsUserConsent();
    }


    public void listeners(){
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                verifyOTP(otp);

            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpTextView.getOTP();
                verifyOTP(otp);
            }
        });

    }


    public void verifyOTP(String otp) {
        otpViewModel.check(phone_number,otp).observe(OTPVerficationActivity.this, s -> {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            if(s.equals("Login Successfull")){

                SharedPref sharedPref = new SharedPref();
                sharedPref.setLoginStatus(OTPVerficationActivity.this,1);

                int status_recom = sharedPref.checkRecommendationStatus(getApplicationContext());

                if(status_recom == 1){
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), Collect_RecommendationActivity.class));
                    finish();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message);
            }
        }
    }
    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            otpTextView.setOTP(matcher.group(0));
        }
    }



    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }



}
