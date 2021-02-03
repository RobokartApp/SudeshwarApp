package com.ark.robokart_robotics.Fragments.AskDoubt;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.MyPostModel;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private final PostRepository postRepository;

    public PostViewModel(@NonNull Application application) {
        super(application);
        postRepository = new PostRepository(application);
    }

    public MutableLiveData<List<MyPostModel>> getMyPostsList(){
        return postRepository.getMyPostsList();
    }
}
