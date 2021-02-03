package com.ark.robokart_robotics.Activities.RegistrationActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class RegistrationViewModel extends AndroidViewModel {

    public RegistrationRepository registrationRepository;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        registrationRepository = new RegistrationRepository(application);
    }

    public MutableLiveData<String> registeruser(String childName, String parentName, String mobile, String parentEmail, String grade, String havePc){
        return registrationRepository.register(childName,parentName,mobile,parentEmail,grade,havePc);
    }
}
