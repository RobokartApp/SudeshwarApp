package com.ark.robokart_robotics.Fragments.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.Adapters.AdvanceCourseListAdpater;
import com.ark.robokart_robotics.Adapters.MyCoursesAdapter;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardViewModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.MyCoursesModel;
import com.ark.robokart_robotics.R;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CoursesFragment extends Fragment {

    private RecyclerView myCoursesRecyclerview;

    private CoursesViewModel coursesViewModel;

    private MyCoursesAdapter myCoursesAdapter;


    public CoursesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesViewModel =  new ViewModelProvider(this).get(CoursesViewModel.class);

        init(view);

        listeners();
    }

    public void init(View v){

        myCoursesRecyclerview = v.findViewById(R.id.myCoursesRecyclerview);

        coursesViewModel.getMyCoursesList("19").observe(getActivity(), new Observer<List<MyCoursesModel>>() {
            @Override
            public void onChanged(List<MyCoursesModel> courseListModels) {
                myCoursesAdapter = new MyCoursesAdapter(getApplicationContext(),courseListModels);
                myCoursesRecyclerview.setAdapter(myCoursesAdapter);
                myCoursesAdapter.notifyDataSetChanged();
            }
        });
    }

    public void listeners(){

    }
}
