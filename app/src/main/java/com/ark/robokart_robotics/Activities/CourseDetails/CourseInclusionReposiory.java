package com.ark.robokart_robotics.Activities.CourseDetails;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.CourseInclusionModel;

import java.util.ArrayList;
import java.util.List;

public class CourseInclusionReposiory {

    private Application application;

    private MutableLiveData<List<CourseInclusionModel>> courseInclusionList = new MutableLiveData<>();

    private ArrayList<CourseInclusionModel> courseInclusionMArrayList = new ArrayList<>();

    public CourseInclusionReposiory(Application application){
        this.application = application;
    }

    public MutableLiveData<List<CourseInclusionModel>> getCourseInclusionList(){
        courseInclusionMArrayList.add(new CourseInclusionModel("1","1","Introduction","2:51"));
        courseInclusionMArrayList.add(new CourseInclusionModel("2","2","Virtual Box Installation","45:51"));
        courseInclusionMArrayList.add(new CourseInclusionModel("3","3","ROS Installation","02:51"));
        courseInclusionMArrayList.add(new CourseInclusionModel("4","4","Exercise: Install ROS on your Machine","02:51"));
        courseInclusionMArrayList.add(new CourseInclusionModel("5","5","Package","02:51"));
        courseInclusionList.setValue(courseInclusionMArrayList);
        return courseInclusionList;
    }

}
