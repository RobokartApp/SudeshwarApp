package com.ark.robokart_robotics.Activities.FreeCourses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Adapters.RecyclerAdapter;
import com.ark.robokart_robotics.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class SingleYouTubeActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    Bundle bundle;
    ArrayList<String> list=new ArrayList<>();
    ArrayList<String> title=new ArrayList<>();
    RecyclerView recyclerView;
TextView headTitle;
ImageView share_vid;
String from;
int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_you_tube);

        init();
        recyclerView.setHasFixedSize(true);
        from="single";
        String[] sAr={list.get(position),title.get(position)};
        RecyclerAdapter adapter = new RecyclerAdapter(this,list,title,from,sAr);
        recyclerView.setAdapter(adapter);

        list.remove(position);title.remove(position);
        adapter.notifyDataSetChanged();
list.add(sAr[0]);
title.add(sAr[1]);


    }

    private void init() {
headTitle=findViewById(R.id.head_titleYoutube);
        recyclerView=findViewById(R.id.youtube_recycler);
        bundle=getIntent().getExtras();
        String id=bundle.getString("id");
        share_vid=findViewById(R.id.share_vid);
        position=bundle.getInt("position");
        //Toast.makeText(this, "position:"+position, Toast.LENGTH_SHORT).show();
        list=bundle.getStringArrayList("list");
        title=bundle.getStringArrayList("title");
        youTubePlayerView=findViewById(R.id.youTubePlayerView);
        headTitle.setText(title.get(position));

        share_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage= "\nRobokart app रोबोटिक्स हो या कोडिंग सब कुछ इतने मजे से सीखते है कि एक बार में सब दिमाग में.. \n" +
                            "खुद ही देखलो.... मान जाओगे \uD83D\uDE07\n\n";
                    shareMessage = shareMessage + "https://robokart.com/app/video";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

          onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(id);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize("UCYChY6QTBzspXa79-4SVEbw",onInitializedListener);
//api key youtube: AIzaSyCv4417xZcCrOgQZ7B85rsV9DItCMor74k

    }

}