package com.ark.robokart_robotics.Activities.View_all_search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ark.robokart_robotics.Model.CourseListModel;

import java.util.List;

public class ViewAllSearchViewModel extends AndroidViewModel {


    private ViewAllSearchRepository viewAllSearchRepository;

    public ViewAllSearchViewModel(@NonNull Application application) {
        super(application);
        viewAllSearchRepository = new ViewAllSearchRepository(application);
    }


    public LiveData<List<CourseListModel>> getCourseList() {
        return viewAllSearchRepository.getCourseList();
    }
}
