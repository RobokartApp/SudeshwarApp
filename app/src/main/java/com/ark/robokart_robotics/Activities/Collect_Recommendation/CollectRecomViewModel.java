package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ark.robokart_robotics.Model.Recommendations;

import java.util.List;

public class CollectRecomViewModel extends AndroidViewModel {

    private CollectRecomRepository collectRecomRepository;


    public CollectRecomViewModel(@NonNull Application application) {
        super(application);
        collectRecomRepository = new CollectRecomRepository(application);
    }

    public LiveData<List<Recommendations>> getCollectRecommendations() {
        return collectRecomRepository.getRecommendationList();
    }
}
