
package com.ark.robokart_robotics.Activities.VideoPlaying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ark.robokart_robotics.Adapters.CommentsAdapter;
import com.ark.robokart_robotics.Adapters.CurriculumAdapter;
import com.ark.robokart_robotics.R;

public class VideoPlayingActivity extends AppCompatActivity {

    private RecyclerView curriculum_recyclerview;

    private RecyclerView comments_Recyclerview;

    private CurriculumAdapter curriculumAdapter;

    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);

        curriculum_recyclerview = findViewById(R.id.curriculum_recyclerview);

        comments_Recyclerview = findViewById(R.id.comments_Recyclerview);

        commentsAdapter = new CommentsAdapter();



        curriculum_recyclerview.setAdapter(curriculumAdapter);
    }
}
