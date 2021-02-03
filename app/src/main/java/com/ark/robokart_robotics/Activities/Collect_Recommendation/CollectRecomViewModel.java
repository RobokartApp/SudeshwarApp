package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Recommendations;

import java.util.List;

public class CollectRecomViewModel extends AndroidViewModel {

    private final CollectRecomRepository collectRecomRepository;


    public CollectRecomViewModel(@NonNull Application application) {
        super(application);
        collectRecomRepository = new CollectRecomRepository(application);
    }

    public MutableLiveData<List<Recommendations>> getCollectRecommendations() {
        return collectRecomRepository.getRecommendationList();
    }

    public MutableLiveData<String> collect(String r_id, String customer_id){
        return collectRecomRepository.collect(r_id,customer_id);
    }
}
