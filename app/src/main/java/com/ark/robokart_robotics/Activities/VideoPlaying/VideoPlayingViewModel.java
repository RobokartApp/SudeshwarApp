package com.ark.robokart_robotics.Activities.VideoPlaying;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.CommentModel;
import com.ark.robokart_robotics.Model.CourseInclusionModel;
import com.ark.robokart_robotics.Model.CurriculumModel;

import java.util.List;

public class VideoPlayingViewModel extends AndroidViewModel {

    public VideoPlayingRepository videoPlayingRepository;

    public VideoPlayingViewModel(@NonNull Application application) {
        super(application);
        videoPlayingRepository = new VideoPlayingRepository(application);
    }

    public MutableLiveData<List<CurriculumModel>> getCurriculumList() {
        return videoPlayingRepository.getCurriculumList();
    }

    public MutableLiveData<List<CommentModel>> getCommentList() {
        return videoPlayingRepository.getCommentList();
    }


}
