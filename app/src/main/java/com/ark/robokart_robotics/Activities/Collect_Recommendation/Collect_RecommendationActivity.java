package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.R;

import java.util.List;

public class Collect_RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecommendationAdapter recommendationAdapter;

    private CollectRecomViewModel collectRecomViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect__recommendation);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recommendation_recyclerview);

        collectRecomViewModel = new ViewModelProvider(this).get(CollectRecomViewModel.class);


        collectRecomViewModel.getCollectRecommendations().observe(this, new Observer<List<Recommendations>>() {
            @Override
            public void onChanged(List<Recommendations> recommendationsList) {
                prepareRecyclerView(recommendationsList);
            }
        });
    }

    private void prepareRecyclerView(List<Recommendations> recommendationsList) {
        recommendationAdapter = new RecommendationAdapter(recommendationsList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recommendationAdapter);
        recommendationAdapter.notifyDataSetChanged();
    }

}
