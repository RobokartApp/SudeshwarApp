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

    public MutableLiveData<List<CurriculumModel>> getCurriculumList(String course_id) {
        return videoPlayingRepository.getCurriculumList(course_id);
    }

    public MutableLiveData<List<CommentModel>> getCommentList(String course_id, String customer_id) {
        return videoPlayingRepository.getCommentList(course_id, customer_id);
    }

    public MutableLiveData<String> postComment(String course_id, String chapter_id, String chapter_name, String comment, String customer_id){
        return videoPlayingRepository.postComment(course_id,chapter_id,chapter_name,comment,customer_id);
    }
}
