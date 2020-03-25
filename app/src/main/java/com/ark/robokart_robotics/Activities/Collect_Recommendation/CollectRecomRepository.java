package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Recommendations;

import java.util.ArrayList;
import java.util.List;

public class CollectRecomRepository {

    private Application application;

    private MutableLiveData<List<Recommendations>> recommendationList = new MutableLiveData<>();

    private ArrayList<Recommendations> recommendationsArrayList = new ArrayList<>();

    public CollectRecomRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<Recommendations>> getRecommendationList(){
        recommendationsArrayList.add(new Recommendations("1","Quadcopter"));
        recommendationsArrayList.add(new Recommendations("2","AI"));
        recommendationsArrayList.add(new Recommendations("3","Robotics"));
        recommendationsArrayList.add(new Recommendations("4","3D Printing"));
        recommendationsArrayList.add(new Recommendations("5","Circuit Design"));
        recommendationsArrayList.add(new Recommendations("6","Circuit Design"));

        recommendationList.setValue(recommendationsArrayList);
        return recommendationList;
    }

}
