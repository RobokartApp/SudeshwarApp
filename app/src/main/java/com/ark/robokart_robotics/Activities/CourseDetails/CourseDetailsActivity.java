package com.ark.robokart_robotics.Activities.CourseDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ark.robokart_robotics.Adapters.CourseInclusionAdapter;
import com.ark.robokart_robotics.Model.CourseInclusionModel;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity {

    ImageView back_btn;

    RecyclerView courseInclusionRecyclerview;

    private CourseInclusionAdapter courseInclusionAdapter;

    private CourseInclusionViewModel courseInclusionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        back_btn = findViewById(R.id.back_btn);

        courseInclusionRecyclerview = findViewById(R.id.courseInclusionRecyclerview);

        courseInclusionViewModel = new ViewModelProvider(this).get(CourseInclusionViewModel.class);


        courseInclusionViewModel.getCourseInclusionList().observe(this, new Observer<List<CourseInclusionModel>>() {
            @Override
            public void onChanged(List<CourseInclusionModel> recommendationsList) {
                prepareRecyclerView(recommendationsList);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void prepareRecyclerView(List<CourseInclusionModel> courseInclusionModelList) {
        courseInclusionAdapter = new CourseInclusionAdapter(getApplicationContext(),courseInclusionModelList);


        courseInclusionRecyclerview.setItemAnimator(new DefaultItemAnimator());
        courseInclusionRecyclerview.setAdapter(courseInclusionAdapter);
        courseInclusionAdapter.notifyDataSetChanged();
    }
}
