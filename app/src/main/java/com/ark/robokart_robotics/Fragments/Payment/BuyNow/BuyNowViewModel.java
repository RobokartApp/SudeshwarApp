package com.ark.robokart_robotics.Fragments.Payment.BuyNow;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.CourseInclusionModel;

import java.util.List;

public class BuyNowViewModel extends AndroidViewModel {

    private BuyNowRepository buyNowRepository;

    public BuyNowViewModel(@NonNull Application application) {
        super(application);
        buyNowRepository = new BuyNowRepository(application);
    }

    public MutableLiveData<String> checkLicenseKey(String login_id, String course_id, String code) {
        return buyNowRepository.checkLicenseKey(login_id,course_id,code);
    }

}
