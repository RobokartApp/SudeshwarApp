package com.ark.robokart_robotics.Activities.CourseDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.Model.CourseInclusionModel;

import java.util.List;

public class CourseInclusionViewModel extends AndroidViewModel {

    public CourseInclusionReposiory courseInclusionReposiory;

    public CourseInclusionViewModel(@NonNull Application application) {
        super(application);
        courseInclusionReposiory = new CourseInclusionReposiory(application);
    }

    public MutableLiveData<List<CourseInclusionModel>> getCourseInclusionList(String courseid) {
        return courseInclusionReposiory.getCourseInclusionList(courseid);
    }

    public MutableLiveData<String> getCourseAccess(String courseid,String customer_id){
        return courseInclusionReposiory.getCourseAccess(courseid, customer_id);
    }

    public MutableLiveData<List<Class_chapters>> getChapterName(String courseid) {
        return courseInclusionReposiory.getChapterName(courseid);
    }
}
