package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.ChooseStandard.StandardActivity;
import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.Button;

public class Collect_RecommendationActivity extends AppCompatActivity {

    private static final String TAG = "Collect_RecommendationA";

    private RecyclerView recyclerView;

    private RecommendationAdapter recommendationAdapter;

    private CollectRecomViewModel collectRecomViewModel;

    private Button btncollect;

    private LottieAnimationView animationView;

    private TextView textview_error, welcome_text;

    private LinearLayout error_layout;
    
    private ArrayList<String> selectedItems = new ArrayList<>();

    String new_recom_string;

    String SEPARATOR = ",";

    String customer_id = "";

    String customer_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect__recommendation);

        btncollect = findViewById(R.id.btncollect);

        animationView = findViewById(R.id.drawable_anim);

        textview_error = findViewById(R.id.textview_error);

        welcome_text = findViewById(R.id.welcome_text);


        error_layout = findViewById(R.id.error_layout);


        recyclerView = findViewById(R.id.recommendation_recyclerview);

        collectRecomViewModel = new ViewModelProvider(this).get(CollectRecomViewModel.class);


        collectRecomViewModel.getCollectRecommendations().observe(this, new Observer<List<Recommendations>>() {
            @Override
            public void onChanged(List<Recommendations> recommendationsList) {
                prepareRecyclerView(recommendationsList);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","0");
        customer_name = sharedPreferences.getString("customer_name","");




        welcome_text.setText("Hey"+ customer_name +", We need to know you \n better to give you the best \n recommendations.");
        listeners();
    }

    public void listeners(){
        btncollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RecommendationAdapter.selectedItemList.size() == 0){
                    error_layout.setVisibility(View.VISIBLE);
                    animationView.setAnimation("error.json");
                    animationView.playAnimation();
                    textview_error.setVisibility(View.VISIBLE);
                }
                else {
                    error_layout.setVisibility(View.GONE);
                    animationView.setVisibility(View.GONE);
                    textview_error.setVisibility(View.GONE);



                    generateRecommendations();
                    

                }

            }
        });
    }


    public void generateRecommendations(){
        new_recom_string = ""; // Always reset


        selectedItems = RecommendationAdapter.selectedItemList;

        new_recom_string = TextUtils.join(",",selectedItems);

//        if(selectedItems.size() > 0) {
//
//            StringBuilder csvBuilder = new StringBuilder();
//            for (String value : selectedItems) {
//                csvBuilder.append(value);
//                csvBuilder.append(",");
//            }
//            new_recom_string = csvBuilder.toString();
//            new_recom_string = new_recom_string.substring(0, new_recom_string.length() - SEPARATOR.length());
////            if (new_food_tags_string.startsWith(",")) {
////                new_food_tags_string = new_food_tags_string.substring(0, 1);
////            }
//        }


        Log.d(TAG, "String selected: "+ new_recom_string);
        collectRecomViewModel.collect(new_recom_string,customer_id).observe(Collect_RecommendationActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
              if(s.equals("Preference Stored")){
                    Toast.makeText(getApplicationContext(),"Thank you for selecting your preferences",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), StandardActivity.class));
                    finish();
                }
                else{

                }
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
