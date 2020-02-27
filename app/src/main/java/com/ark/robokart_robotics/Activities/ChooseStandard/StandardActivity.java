package com.ark.robokart_robotics.Activities.ChooseStandard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ark.robokart_robotics.Activities.Collect_Recommendation.CollectRecomViewModel;
import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Adapters.StandardAdapter;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.Model.StandardModel;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class StandardActivity extends AppCompatActivity {

    private RecyclerView intermediate_recyclerview, advanced_recyclerview;

    private StandardAdapter standardAdapter;

    private StandardViewModel standardViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        intermediate_recyclerview = findViewById(R.id.intermediate_recyclerview);
        advanced_recyclerview = findViewById(R.id.advanced_recyclerview);

        standardViewModel = new ViewModelProvider(this).get(StandardViewModel.class);


        standardViewModel.getCollectRecommendations().observe(this, new Observer<List<StandardModel>>() {
            @Override
            public void onChanged(List<StandardModel> standardModels) {
                prepareRecyclerView(standardModels);
            }
        });
    }

    private void prepareRecyclerView(List<StandardModel> standardList) {
        standardAdapter = new StandardAdapter(standardList);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.canScrollVertically();
        intermediate_recyclerview.setLayoutManager(layoutManager);


        intermediate_recyclerview.setItemAnimator(new DefaultItemAnimator());
        intermediate_recyclerview.setAdapter(standardAdapter);
        standardAdapter.notifyDataSetChanged();
    }
}
