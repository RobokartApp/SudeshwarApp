package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

import carbon.widget.Button;

public class Collect_RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecommendationAdapter recommendationAdapter;

    private CollectRecomViewModel collectRecomViewModel;

    private Button btncollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect__recommendation);

        btncollect = findViewById(R.id.btncollect);



        recyclerView = findViewById(R.id.recommendation_recyclerview);

        collectRecomViewModel = new ViewModelProvider(this).get(CollectRecomViewModel.class);


        collectRecomViewModel.getCollectRecommendations().observe(this, new Observer<List<Recommendations>>() {
            @Override
            public void onChanged(List<Recommendations> recommendationsList) {
                prepareRecyclerView(recommendationsList);
            }
        });

        listeners();
    }

    public void listeners(){
        btncollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }

    private void prepareRecyclerView(List<Recommendations> recommendationsList) {
        recommendationAdapter = new RecommendationAdapter(recommendationsList);



        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.canScrollVertically();
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recommendationAdapter);
        recommendationAdapter.notifyDataSetChanged();
    }

}
