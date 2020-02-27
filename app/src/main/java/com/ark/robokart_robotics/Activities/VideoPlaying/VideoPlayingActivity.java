
package com.ark.robokart_robotics.Activities.VideoPlaying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ark.robokart_robotics.Adapters.CommentsAdapter;
import com.ark.robokart_robotics.Adapters.CourseListAdapter;
import com.ark.robokart_robotics.Adapters.CurriculumAdapter;
import com.ark.robokart_robotics.Adapters.CustomAdapter;
import com.ark.robokart_robotics.Adapters.IntermediateCourseListAdapter;
import com.ark.robokart_robotics.Model.CommentModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.CurriculumModel;
import com.ark.robokart_robotics.R;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class VideoPlayingActivity extends AppCompatActivity {

    private RecyclerView curriculum_recyclerview;

    private RecyclerView comments_Recyclerview;

    private CurriculumAdapter curriculumAdapter;

    private CommentsAdapter commentsAdapter;

    public VideoPlayingViewModel videoPlayingViewModel;

    public CommentModel commentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);

        curriculum_recyclerview = findViewById(R.id.curriculum_recyclerview);

        comments_Recyclerview = findViewById(R.id.comments_Recyclerview);

        videoPlayingViewModel = new ViewModelProvider(this).get(VideoPlayingViewModel.class);


        videoPlayingViewModel.getCurriculumList().observe(this, new Observer<List<CurriculumModel>>() {
            @Override
            public void onChanged(List<CurriculumModel> curriculumModels) {
                prepareCurrRecyclerView(curriculumModels);
            }
        });



        videoPlayingViewModel.getCommentList().observe(this, new Observer<List<CommentModel>>() {
            @Override
            public void onChanged(List<CommentModel> commentModelList) {
                prepareCommentRecyclerview(commentModelList);
            }
        });



    }

    private void prepareCurrRecyclerView(List<CurriculumModel> curriculumModelList) {
        curriculumAdapter = new CurriculumAdapter(getApplicationContext(),curriculumModelList);
        curriculum_recyclerview.setAdapter(curriculumAdapter);
         curriculumAdapter.notifyDataSetChanged();
    }

    private void prepareCommentRecyclerview(List<CommentModel> commentModelList){
        commentsAdapter = new CommentsAdapter(getApplicationContext(),commentModelList);
        comments_Recyclerview.setAdapter(commentsAdapter);
        commentsAdapter.notifyDataSetChanged();
    }
}
