package com.ark.robokart_robotics.Fragments.Courses;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.Adapters.AdvanceCourseListAdpater;
import com.ark.robokart_robotics.Adapters.MyCoursesAdapter;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardViewModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.MyCoursesModel;
import com.ark.robokart_robotics.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CoursesFragment extends Fragment {

    private RecyclerView myCoursesRecyclerview;

    private CoursesViewModel coursesViewModel;

    private MyCoursesAdapter myCoursesAdapter;

    private LottieAnimationView animationView;

    String customer_id="";


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

        animationView = v.findViewById(R.id.animationView);

        myCoursesRecyclerview = v.findViewById(R.id.myCoursesRecyclerview);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userdetails",MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","0");

        coursesViewModel.getMyCoursesList(customer_id).observe(getActivity(), new Observer<List<MyCoursesModel>>() {
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
