package com.ark.robokart_robotics.Activities.View_all_search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ark.robokart_robotics.Adapters.CourseListAdapter;
import com.ark.robokart_robotics.Adapters.IntermediateCourseListAdapter;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardViewModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;

import java.util.List;

public class ViewAllSearchActivity extends AppCompatActivity {

    public  RecyclerView coursesRecyclerview;

    private ImageView back_btn;

    private ViewAllSearchViewModel viewAllSearchViewModel;

    public  CourseListAdapter courseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_search);

        init();

        listeners();

    }


    public void init(){
        back_btn = findViewById(R.id.back_btn);

        coursesRecyclerview = findViewById(R.id.coursesRecyclerview);


        viewAllSearchViewModel = new ViewModelProvider(this).get(ViewAllSearchViewModel.class);

        viewAllSearchViewModel.getCourseList().observe(this, new Observer<List<CourseListModel>>() {
            @Override
            public void onChanged(List<CourseListModel> courseListModelList) {
                prepareRecyclerView(courseListModelList);
            }
        });
    }

    public void listeners(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void prepareRecyclerView(List<CourseListModel> courseListModelList) {
        courseListAdapter = new CourseListAdapter(getApplicationContext(),courseListModelList);
        coursesRecyclerview.setItemAnimator(new DefaultItemAnimator());
        coursesRecyclerview.setAdapter(courseListAdapter);
        courseListAdapter.notifyDataSetChanged();

    }
}
