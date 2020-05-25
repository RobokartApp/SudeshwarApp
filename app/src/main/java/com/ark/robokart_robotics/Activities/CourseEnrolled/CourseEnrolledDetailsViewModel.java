package com.ark.robokart_robotics.Activities.CourseEnrolled;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.ChapterName;

import java.util.List;

public class CourseEnrolledDetailsViewModel extends AndroidViewModel {

    private CourseEnrolledDetailsRepository courseEnrolledDetailsRepository;

    public CourseEnrolledDetailsViewModel(@NonNull Application application) {
        super(application);
        courseEnrolledDetailsRepository = new CourseEnrolledDetailsRepository(application);
    }

    public MutableLiveData<List<ChapterName>> getChapterName(String courseid) {
        return courseEnrolledDetailsRepository.getChapterName(courseid);
    }

    public MutableLiveData<List<ChapterContent>> getChapterContent(String courseid){
        return courseEnrolledDetailsRepository.getChapterContent(courseid);
    }
}
