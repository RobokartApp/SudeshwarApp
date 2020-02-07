package com.ark.robokart_robotics.Fragments.Dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ark.robokart_robotics.Model.CourseListModel;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private DashboardRepository dashboardRepository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
    }

    public LiveData<List<CourseListModel>> getCourseList() {
        return dashboardRepository.getCourseList();
    }
}