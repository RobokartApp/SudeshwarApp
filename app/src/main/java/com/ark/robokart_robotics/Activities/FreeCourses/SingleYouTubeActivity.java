package com.ark.robokart_robotics.Activities.FreeCourses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Adapters.RecyclerAdapter;
import com.ark.robokart_robotics.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.common.base.Stopwatch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    SharedPreferences sharedpreferences;

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
    public Stopwatch stopwatch;int time=0;

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

         sharedpreferences= getSharedPreferences("level_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

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


        stopwatch=Stopwatch.createStarted();

          onInitializedListener=new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(id);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        Log.e("playback event","playing");
                        if (!stopwatch.isRunning())
                            stopwatch.start();
                    }

                    @Override
                    public void onPaused() {
                        time+=stopwatch.elapsed(TimeUnit.SECONDS);
                        Log.e("playback event","paused:"+time);

                        if(stopwatch.isRunning())
                            stopwatch.stop();

                        int old_time=sharedpreferences.getInt("time",1);
                        editor.putInt("time",(time+old_time));
                        editor.apply();
                    }

                    @Override
                    public void onStopped() {
                        //time+=stopwatch.elapsed(TimeUnit.SECONDS);

                        //int old_time=sharedpreferences.getInt("time",1);
                        //editor.putInt("time",(time+old_time));
                        //editor.apply();

                        Log.e("playback event","stopped old:"+"&new:"+time);
                    }

                    @Override
                    public void onBuffering(boolean b) {
                        Log.e("playback event","buffering");
                        if(stopwatch.isRunning())
                            stopwatch.stop();
                    }

                    @Override
                    public void onSeekTo(int i) {
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize("UCYChY6QTBzspXa79-4SVEbw",onInitializedListener);
//api key youtube: AIzaSyCv4417xZcCrOgQZ7B85rsV9DItCMor74k

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}