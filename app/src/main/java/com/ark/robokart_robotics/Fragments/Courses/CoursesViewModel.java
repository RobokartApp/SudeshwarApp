package com.ark.robokart_robotics.Fragments.Courses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.MyCoursesModel;

import java.util.List;

public class CoursesViewModel extends AndroidViewModel {

    private CoursesRepository coursesRepository;

    public CoursesViewModel(@NonNull Application application) {
        super(application);
        coursesRepository = new CoursesRepository(application);
    }

    public MutableLiveData<List<MyCoursesModel>> getMyCoursesList(String customer_id){
        return coursesRepository.getMyCoursesList(customer_id);
    }
}
