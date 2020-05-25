package com.ark.robokart_robotics.Activities.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository profileRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public MutableLiveData<String> update(String User_id, String customer_name, String email, String mobile, String parentmoblie){
        return profileRepository.updateProfile(User_id,customer_name,email,mobile,parentmoblie);
    }
}