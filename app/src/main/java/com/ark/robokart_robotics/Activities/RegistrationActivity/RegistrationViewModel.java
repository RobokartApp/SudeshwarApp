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

    public MutableLiveData<String> registeruser(String fullname, String email, String password, String refer_code, String student_number, String parent_number, String username){
        return registrationRepository.register(fullname,email,password,refer_code,student_number,parent_number, username);
    }
}
