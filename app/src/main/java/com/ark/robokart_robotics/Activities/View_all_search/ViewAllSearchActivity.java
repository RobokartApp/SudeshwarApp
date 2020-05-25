package com.ark.robokart_robotics.Activities.View_all_search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ark.robokart_robotics.Adapters.CourseListAdapter;
import com.ark.robokart_robotics.Adapters.IntermediateCourseListAdapter;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardViewModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

public class ViewAllSearchActivity extends AppCompatActivity {

    private List<CourseListModel> courseListModelArrayList = new ArrayList<>();

    public  RecyclerView coursesRecyclerview;

    private ImageView back_btn;

    private EditText edt_search;

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

        edt_search = findViewById(R.id.edt_search);

        coursesRecyclerview = findViewById(R.id.coursesRecyclerview);


        viewAllSearchViewModel = new ViewModelProvider(this).get(ViewAllSearchViewModel.class);

        viewAllSearchViewModel.getCourseList().observe(this, new Observer<List<CourseListModel>>() {
            @Override
            public void onChanged(List<CourseListModel> courseListModelList) {

                courseListModelArrayList = courseListModelList;

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

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String text) {
        ArrayList<CourseListModel> filteredList = new ArrayList<>();

        for (CourseListModel item : courseListModelArrayList) {
            if (item.getCourse_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        courseListAdapter.filterList(filteredList);
    }


    private void prepareRecyclerView(List<CourseListModel> courseListModelList) {
        courseListAdapter = new CourseListAdapter(getApplicationContext(),courseListModelList);
        coursesRecyclerview.setItemAnimator(new DefaultItemAnimator());
        coursesRecyclerview.setAdapter(courseListAdapter);
        courseListAdapter.notifyDataSetChanged();
    }
}
