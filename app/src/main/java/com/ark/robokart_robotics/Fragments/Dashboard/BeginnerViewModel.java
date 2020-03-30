package com.ark.robokart_robotics.Fragments.Dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ark.robokart_robotics.Model.CourseListModel;

import java.util.List;

public class BeginnerViewModel extends AndroidViewModel {

    private BeginnerRepository beginnerRepository;

    public BeginnerViewModel(@NonNull Application application) {
        super(application);
        beginnerRepository = new BeginnerRepository(application);
    }

    public LiveData<List<CourseListModel>> getBeginnerourseList() {
        return beginnerRepository.getBeginnerCourseList();
    }
}
