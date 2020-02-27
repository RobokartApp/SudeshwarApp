package com.ark.robokart_robotics.Activities.VideoPlaying;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.CommentModel;
import com.ark.robokart_robotics.Model.CurriculumModel;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayingRepository {

    private Application application;

    private MutableLiveData<List<CurriculumModel>> curriculumList = new MutableLiveData<>();

    private ArrayList<CurriculumModel> curriculumModelArrayList = new ArrayList<>();

    private MutableLiveData<List<CommentModel>> commentList = new MutableLiveData<>();

    private ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();


    public VideoPlayingRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<CurriculumModel>> getCurriculumList(){
        curriculumModelArrayList.add(new CurriculumModel("New Doc","newdoc.pdf"));
        curriculumList.setValue(curriculumModelArrayList);
        return  curriculumList;
    }


    public MutableLiveData<List<CommentModel>> getCommentList(){
        commentModelArrayList.add(new CommentModel("John Doe","Good Course!!"));
        commentList.setValue(commentModelArrayList);
        return  commentList;
    }

}
