package com.ark.robokart_robotics.Fragments.Dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ark.robokart_robotics.Model.CourseListModel;

import java.util.List;

public class IntermediateViewModel extends AndroidViewModel {

    private IntermediateRepository intermediateRepository;

    public IntermediateViewModel(@NonNull Application application) {
        super(application);
        intermediateRepository = new IntermediateRepository(application);
    }

    public LiveData<List<CourseListModel>> getIntermediateCourseList() {
        return intermediateRepository.getIntermediateCourseList();
    }
}
