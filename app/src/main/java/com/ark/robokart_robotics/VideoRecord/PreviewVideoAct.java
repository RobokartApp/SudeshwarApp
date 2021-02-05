package com.ark.robokart_robotics.VideoRecord;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.R;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.daasuu.gpuv.player.PlayerScaleType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PreviewVideoAct extends AppCompatActivity implements Player.EventListener {
//

        String video_url;
        StyledPlayerView playerView;
        public static int  select_postion=0;

        RecyclerView recylerview;

        String draft_file,path1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_preview_video);
playerView=findViewById(R.id.layout_movie_wrapper);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
         //Toast.makeText(this, "This is pev act", Toast.LENGTH_SHORT).show();
   /*         Intent intent=getIntent();
            if(intent.hasExtra("video_path")){
                path1=intent.getStringExtra("video_path");
                //draft_file=intent.getStringExtra("draft_file");

            }else
                Toast.makeText(this, "intent not found!", Toast.LENGTH_SHORT).show();
*/
            select_postion=0;
            video_url= Variables.outputfile2;

            //Toast.makeText(this, "Came for preview: "+video_url, Toast.LENGTH_SHORT).show();
            Log.d("Prev vid path",video_url);

            findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

                }
            });


            findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(select_postion==0){
                        GotopostScreen();
/*
                        try {
                           // Functions.copyFile(new File(Variables.outputfile2),
                             //       new File(Variables.output_filter_file));
                            GotopostScreen();
                        }

                        catch (IOException e) {
                            e.printStackTrace();
                            Log.d(Variables.tag,e.toString());
                            Save_Video(Variables.outputfile2,Variables.output_filter_file);
                        }
*/
                    }
                   // else
                        //Save_Video(Variables.outputfile2,Variables.output_filter_file);
                }
            });

            Set_Player(video_url);

        }




        // this function will set the player to the current video
        SimpleExoPlayer player;
    public void Set_Player(String path){
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        // Bind the player to the view.
        playerView.setPlayer(player);

        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(path));
// Set the media item to be played.
        player.setMediaItem(mediaItem);
// Prepare the player.
        player.prepare();
// Start the playback.
        player.play();

// Prepare the player.
        player.prepare();
// Start the playback.
        player.play();
/*
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Robokart"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(path));
                //.createMediaSource(Uri.parse("https://robokart.com/app/videos/testvid.mp4"));

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);

        player.setPlayWhenReady(true);

        gpuPlayerView = new GPUPlayerView(this);

        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
       // String rotation=metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
/*
        if(rotation!=null && rotation.equalsIgnoreCase("0")){
            gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_WIDTH);
        }
        else

            gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_NONE);



        gpuPlayerView.setSimpleExoPlayer(player);
        //gpuPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //(findViewById(R.id.layout_movie_wrapper));

        gpuPlayerView.onResume();
*/
    }

        // this is lifecyle of the Activity which is importent for play,pause video or relaese the player

        @Override
        protected void onStop() {
            super.onStop();
            if(player!=null){
                player.setPlayWhenReady(false);
            }

        }

        @Override
        protected void onStart() {
            super.onStart();

            if(player!=null){
                player.setPlayWhenReady(true);
            }

        }

        @Override
        protected void onRestart() {
            super.onRestart();
            if(player!=null){
                player.setPlayWhenReady(true);
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();


            if(player!=null){
                player.removeListener(PreviewVideoAct.this);
                player.release();
                player=null;
            }
        }

        // this function will add the filter to video and save that same video for post the video in post video screen
  /*      public void Save_Video(String srcMp4Path, final String destMp4Path){

            Functions.Show_determinent_loader(this,false,false);
            new GPUMp4Composer(srcMp4Path, destMp4Path)
                    .listener(new GPUMp4Composer.Listener() {
                        @Override
                        public void onProgress(double progress) {

                            Log.d("resp",""+(int) (progress*100));
                            Functions.Show_loading_progress((int)(progress*100));



                        }

                        @Override
                        public void onCompleted() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Functions.cancel_determinent_loader();

                                    GotopostScreen();


                                }
                            });


                        }

                        @Override
                        public void onCanceled() {
                            Log.d("resp", "onCanceled");
                        }

                        @Override
                        public void onFailed(Exception exception) {

                            Log.d("resp",exception.toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        Functions.cancel_determinent_loader();

                                        Toast.makeText(PreviewVideoAct.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    }catch (Exception e){
                                        Toast.makeText(PreviewVideoAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    })
                    .start();


        }

*/

        public void GotopostScreen(){

            Intent intent =new Intent(PreviewVideoAct.this,PostVideoActivity.class);
            //intent.putExtra("vid uri",draft_file);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }



        // Bottom all the function and the Call back listener of the Expo player
        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }


        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }


        @Override
        public void onBackPressed() {

            finish();
            //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

        }
    }
