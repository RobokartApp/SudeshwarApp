package com.ark.robokart_robotics.Activities.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    private final ProfileRepository profileRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public MutableLiveData<String> update(String User_id, String customer_name, String email, String mobile, String parentmoblie,
                                          String customer_image, String username,String bio){
        return profileRepository.updateProfile(User_id,customer_name,email,mobile,parentmoblie, customer_image, username, bio);
    }
}
