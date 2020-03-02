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

import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.R;

public class CoursesFragment extends Fragment {

    RelativeLayout rlSchoolCuriculum;

    public CoursesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        listeners();
    }

    public void init(View v){
        rlSchoolCuriculum = v.findViewById(R.id.rlSchoolCuriculum);
    }

    public void listeners(){
        rlSchoolCuriculum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().popBackStack();
                startActivity(new Intent(getActivity(), QuizActivity.class));
            }
        });
    }
}
