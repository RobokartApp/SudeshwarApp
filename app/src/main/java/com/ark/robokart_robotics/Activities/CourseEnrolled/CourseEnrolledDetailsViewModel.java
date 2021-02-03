package com.ark.robokart_robotics.Activities.CourseEnrolled;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Class_chapters;

import java.util.List;

public class CourseEnrolledDetailsViewModel extends AndroidViewModel {

    private final CourseEnrolledDetailsRepository courseEnrolledDetailsRepository;

    public CourseEnrolledDetailsViewModel(@NonNull Application application) {
        super(application);
        courseEnrolledDetailsRepository = new CourseEnrolledDetailsRepository(application);
    }

    public MutableLiveData<List<Class_chapters>> getChapterName(String courseid) {
        return courseEnrolledDetailsRepository.getChapterName(courseid);
    }

    public MutableLiveData<String> getCourseAccess(String courseid, String customer_id){
        return courseEnrolledDetailsRepository.getCourseAccess(courseid, customer_id);
    }
}
