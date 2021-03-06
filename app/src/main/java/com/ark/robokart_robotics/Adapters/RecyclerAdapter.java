package com.ark.robokart_robotics.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.FreeCourses.SingleYouTubeActivity;
import com.ark.robokart_robotics.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;


/**
 * Created by ofaroque on 8/13/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VideoInfoHolder> {

    //these ids are the unique id for each video
    ArrayList<String> VideoID;// = {"ALTLequqJQk", "2MDVqbA-170", "na3_0VmTiAw"};
    ArrayList<String> title;
    Context ctx;
    String from;
    String[] sAr;
    private static final String KEY = "AIzaSyCv4417xZcCrOgQZ7B85rsV9DItCMor74k";


    public RecyclerAdapter(Context context,ArrayList<String> vidId,ArrayList<String> title,String from,String[] sAr) {
        this.ctx = context;
        this.sAr=sAr;
        this.VideoID=vidId;
        this.title=title;
        this.from=from;
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_youtube_list, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.youTubeThumbnailView.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(VideoID.get(position));

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                holder.videosTitleTextView.setText(title.get(position));
                //youTubeThumbnailLoader.release();
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });
    }


    @Override
    public int getItemCount() {
        return VideoID.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;
        protected TextView videosTitleTextView;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            playButton = itemView.findViewById(R.id.btnYoutube_player);
            videosTitleTextView = itemView.findViewById(R.id.titleYoutube);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
        }

        @Override
        public void onClick(View v) {

            //Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, KEY, VideoID[getLayoutPosition()]);
            //ctx.startActivity(intent);

            String id=VideoID.get(getLayoutPosition());
                    Intent intent=new Intent(ctx, SingleYouTubeActivity.class);
                    if (from.equals("single")) {
                       ((Activity)ctx).finish();
                    }

                    intent.putExtra("id",id);
                    intent.putExtra("list",VideoID);
                    intent.putExtra("title",title);
                    intent.putExtra("position",getLayoutPosition());
ctx.startActivity(intent);
            //youTubePlayerView.initialize("AIzaSyCv4417xZcCrOgQZ7B85rsV9DItCMor74k",onInitializedListener);
            //youTubeThumbnailView.setVisibility(View.GONE);
        }
    }

}