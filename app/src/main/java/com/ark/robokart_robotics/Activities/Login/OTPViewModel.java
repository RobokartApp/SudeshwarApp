package com.ark.robokart_robotics.Activities.Login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class OTPViewModel extends AndroidViewModel {

    public OTPRepository otpRepository;


    public OTPViewModel(@NonNull Application application) {
        super(application);
        otpRepository = new OTPRepository(application);
    }

    public MutableLiveData<String> check(String phone_number, String otp){
        return otpRepository.checkOTP(phone_number,otp);
    }
}
