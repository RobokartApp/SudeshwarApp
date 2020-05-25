package com.ark.robokart_robotics.Activities.ChooseStandard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.StandardModel;

import java.util.List;

public class StandardViewModel extends AndroidViewModel {

    private StandardRepository standardRepository;


    public StandardViewModel(@NonNull Application application) {
        super(application);
        standardRepository = new StandardRepository(application);
    }

    public MutableLiveData<List<StandardModel>> getStandards() {
        return standardRepository.getStandardList();
    }

    public MutableLiveData<Integer> selectStandard(String customer_id, String customer_std, String customer_school_code){
        return standardRepository.selectStandard(customer_id,customer_std,customer_school_code);
    }
}
