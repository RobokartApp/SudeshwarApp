package com.ark.robokart_robotics.Activities.ChooseStandard;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.StandardModel;

import java.util.ArrayList;
import java.util.List;

public class AdvanceStandardRepository {
    private Application application;

    private MutableLiveData<List<StandardModel>> standardList = new MutableLiveData<>();

    private ArrayList<StandardModel> standardArrayList = new ArrayList<>();

    public AdvanceStandardRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<StandardModel>> getAdvancedList() {
        standardArrayList.add(new StandardModel("1", "GMAT"));
        standardArrayList.add(new StandardModel("2", "CAT"));
        standardArrayList.add(new StandardModel("3", "IAS"));
        standardArrayList.add(new StandardModel("4", "GRE"));
        standardList.setValue(standardArrayList);
        return standardList;
    }
}
