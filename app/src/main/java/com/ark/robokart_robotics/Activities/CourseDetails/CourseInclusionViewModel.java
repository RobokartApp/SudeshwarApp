package com.ark.robokart_robotics.Activities.CourseDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.CourseInclusionModel;

import java.util.List;

public class CourseInclusionViewModel extends AndroidViewModel {

    public CourseInclusionReposiory courseInclusionReposiory;

    public CourseInclusionViewModel(@NonNull Application application) {
        super(application);
        courseInclusionReposiory = new CourseInclusionReposiory(application);
    }

    public MutableLiveData<List<CourseInclusionModel>> getCourseInclusionList() {
        return courseInclusionReposiory.getCourseInclusionList();
    }

    public MutableLiveData<List<String>> getCourseDetails(String courseid){
        return courseInclusionReposiory.getCourseDetails(courseid);
    }
}
