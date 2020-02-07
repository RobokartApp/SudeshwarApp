package com.ark.robokart_robotics.Fragments.Dashboard;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.CourseListModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepository {

    private Application application;

    private MutableLiveData<List<CourseListModel>> courseListModelMutableLiveData = new MutableLiveData<>();

    private ArrayList<CourseListModel> courseListModelArrayList = new ArrayList<>();

    public DashboardRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<CourseListModel>> getCourseList(){
        courseListModelArrayList.add(new CourseListModel("1","Robotrix Robotics","4.5","25"));
        courseListModelArrayList.add(new CourseListModel("2","Kindertronics - Electronics","4.95","80"));
        courseListModelArrayList.add(new CourseListModel("3","Brain X - Arduino Based","5","40"));
        courseListModelMutableLiveData.setValue(courseListModelArrayList);
        return courseListModelMutableLiveData;
    }
}
