package com.ark.robokart_robotics.Activities.ChooseStandard;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.StandardModel;

import java.util.ArrayList;
import java.util.List;

public class StandardRepository {

    private Application application;

    private MutableLiveData<List<StandardModel>> standardList = new MutableLiveData<>();

    private ArrayList<StandardModel> standardArrayList = new ArrayList<>();

    public StandardRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<StandardModel>> getStandardList() {
        standardArrayList.add(new StandardModel("1", "VI"));
        standardArrayList.add(new StandardModel("2", "VII"));
        standardArrayList.add(new StandardModel("3", "VIII"));
        standardArrayList.add(new StandardModel("4", "IX"));
        standardArrayList.add(new StandardModel("5", "X"));
        standardArrayList.add(new StandardModel("6", "XI"));
        standardArrayList.add(new StandardModel("7", "XII"));
        standardList.setValue(standardArrayList);
        return standardList;
    }


}
