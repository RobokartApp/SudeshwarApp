package com.ark.robokart_robotics.Activities.ChooseStandard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.Collect_Recommendation.CollectRecomViewModel;
import com.ark.robokart_robotics.Activities.Collect_Recommendation.Collect_RecommendationActivity;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Adapters.StandardAdapter;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.Model.StandardModel;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import carbon.widget.Button;

public class StandardActivity extends AppCompatActivity {

    private static final String TAG = "StandardActivity";

    private RecyclerView intermediate_recyclerview, advanced_recyclerview;

    private StandardAdapter standardAdapter;

    private StandardViewModel standardViewModel;

    private AdvanceStandardViewModel advanceStandardViewModel;

    private Button get_started_btn;

    private LottieAnimationView animationView;

    private TextView textview_error, tvGood;

    private LinearLayout error_layout;

    private ArrayList<String> selectedItems = new ArrayList<>();

    private EditText code_edt;

    String std;

    String customer_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        get_started_btn = findViewById(R.id.get_started_btn);


        intermediate_recyclerview = findViewById(R.id.intermediate_recyclerview);
        advanced_recyclerview = findViewById(R.id.advanced_recyclerview);

        animationView = findViewById(R.id.drawable_anim);

        textview_error = findViewById(R.id.textview_error);

        code_edt = findViewById(R.id.code_edt);

        tvGood = findViewById(R.id.tvGood);


        error_layout = findViewById(R.id.error_layout);


        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","0");

        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        //Set greeting
        String greeting = null;
        if(hour>=6 && hour<12){
            greeting = "Good Morning,";
        } else if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon,";
        } else if(hour >= 17 && hour < 24){
            greeting = "Good Evening,";
        }
        else if(hour >= 0 && hour < 4){
            greeting = "Good Night,";
        }

        tvGood.setText(greeting);

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
                if(standardAdapter.stdselectedList.size() == 0){
                    error_layout.setVisibility(View.VISIBLE);
                    animationView.setAnimation("error.json");
                    animationView.playAnimation();
                    textview_error.setVisibility(View.VISIBLE);
                }
                else {
                    error_layout.setVisibility(View.GONE);
                    animationView.setVisibility(View.GONE);
                    textview_error.setVisibility(View.GONE);
                    setStandard();
                }


            }
        });

    }

    public void setStandard(){
        std = ""; // Always reset

        String school_code = code_edt.getText().toString();

        selectedItems = StandardAdapter.stdselectedList;

        if(selectedItems.size() > 0){
            std = TextUtils.join(",",selectedItems);

        }



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

//
        Log.d(TAG, "String selected: "+ std);
        standardViewModel.selectStandard(customer_id,std,school_code).observe(StandardActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer != 0){
                    Toast.makeText(getApplicationContext(),"You are all set!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{

                }
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
