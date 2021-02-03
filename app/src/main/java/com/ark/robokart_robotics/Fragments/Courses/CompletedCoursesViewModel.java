package com.ark.robokart_robotics.Fragments.Courses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.MyCoursesModel;

import java.util.List;

public class CompletedCoursesViewModel extends AndroidViewModel {

    private final CoursesRepository coursesRepository;

    public CompletedCoursesViewModel(@NonNull Application application) {
        super(application);
        coursesRepository = new CoursesRepository(application);
    }



    public MutableLiveData<List<MyCoursesModel>> getMyCoursesListComplete(String customer_id){
        return coursesRepository.getMyCoursesListComplete(customer_id);
    }


}
