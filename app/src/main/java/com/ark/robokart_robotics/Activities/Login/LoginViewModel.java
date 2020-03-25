package com.ark.robokart_robotics.Activities.Login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class LoginViewModel extends AndroidViewModel {

    public LoginRepository loginRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
    }

    public MutableLiveData<String> requestotp(String phone_number) {
        return loginRepository.requestOTP(phone_number);
    }

}
