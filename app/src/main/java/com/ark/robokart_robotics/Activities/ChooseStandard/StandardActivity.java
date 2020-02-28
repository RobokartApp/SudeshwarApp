package com.ark.robokart_robotics.Activities.ChooseStandard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ark.robokart_robotics.Activities.Collect_Recommendation.CollectRecomViewModel;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Adapters.StandardAdapter;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.Model.StandardModel;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

import carbon.widget.Button;

public class StandardActivity extends AppCompatActivity {

    private RecyclerView intermediate_recyclerview, advanced_recyclerview;

    private StandardAdapter standardAdapter;

    private StandardViewModel standardViewModel;

    private AdvanceStandardViewModel advanceStandardViewModel;

    private Button get_started_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        get_started_btn = findViewById(R.id.get_started_btn);

        intermediate_recyclerview = findViewById(R.id.intermediate_recyclerview);
        advanced_recyclerview = findViewById(R.id.advanced_recyclerview);

        standardViewModel = new ViewModelProvider(this).get(StandardViewModel.class);

        advanceStandardViewModel = new ViewModelProvider(this).get(AdvanceStandardViewModel.class);

        standardViewModel.getStandards().observe(this, new Observer<List<StandardModel>>() {
            @Override
            public void onChanged(List<StandardModel> standardModels) {
                prepareRecyclerView(standardModels);
            }
        });

        advanceStandardViewModel.getAdvance().observe(this, new Observer<List<StandardModel>>() {
            @Override
            public void onChanged(List<StandardModel> standardModels) {
                prepareAdvanceRecyclerView(standardModels);
            }
        });

        get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
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


    private void prepareAdvanceRecyclerView(List<StandardModel> standardList) {

        standardAdapter = new StandardAdapter(standardList);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.canScrollVertically();
        advanced_recyclerview.setLayoutManager(layoutManager);

        advanced_recyclerview.setItemAnimator(new DefaultItemAnimator());
        advanced_recyclerview.setAdapter(standardAdapter);
        standardAdapter.notifyDataSetChanged();
    }
}
