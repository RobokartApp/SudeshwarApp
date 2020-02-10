package com.ark.robokart_robotics.Fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.ark.robokart_robotics.Activities.View_all_search.ViewAllSearchActivity;
import com.ark.robokart_robotics.Adapters.CourseListAdapter;
import com.ark.robokart_robotics.Adapters.IntermediateCourseListAdapter;
import com.ark.robokart_robotics.Adapters.RecommendationAdapter;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardViewModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DashboardFragment extends Fragment {


    private RecyclerView rvJustStartingVideos;
    private RecyclerView rvKnowBitVideos;

    private DashboardViewModel dashboardViewModel;

    private CourseListAdapter courseListAdapter;

    private IntermediateCourseListAdapter intermediateCourseListAdapter;

    private RelativeLayout parent;

    private TextView view_all_js, view_all_iknow_just_bits;

    public DashboardFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = view.findViewById(R.id.parent);
        view_all_js = view.findViewById(R.id.view_all_js);
        view_all_iknow_just_bits = view.findViewById(R.id.view_all_iknow_just_bits);
        rvJustStartingVideos = view.findViewById(R.id.rvJustStartingVideos);
        rvKnowBitVideos = view.findViewById(R.id.rvKnowBitVideos);






        dashboardViewModel =  new ViewModelProvider(this).get(DashboardViewModel.class);


        dashboardViewModel.getCourseList().observe(getActivity(), new Observer<List<CourseListModel>>() {
            @Override
            public void onChanged(List<CourseListModel> courseListModelList) {
                prepareRecyclerView(courseListModelList);
            }
        });


        view_all_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewAllSearchActivity.class));
            }
        });
    }

    private void prepareRecyclerView(List<CourseListModel> courseListModelList) {
        courseListAdapter = new CourseListAdapter(courseListModelList);
        intermediateCourseListAdapter = new IntermediateCourseListAdapter(courseListModelList);

        rvJustStartingVideos.setItemAnimator(new DefaultItemAnimator());
        rvJustStartingVideos.setAdapter(courseListAdapter);
        rvKnowBitVideos.setAdapter(intermediateCourseListAdapter);
        courseListAdapter.notifyDataSetChanged();


    }
}