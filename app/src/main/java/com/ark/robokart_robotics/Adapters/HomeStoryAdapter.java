package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.FreeCourses.SingleYouTubeActivity;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Fragments.Stories.StoriesFragment;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by ofaroque on 8/13/15.
 */
public class HomeStoryAdapter extends RecyclerView.Adapter<HomeStoryAdapter.VideoInfoHolder> {

    //these ids are the unique id for each video
    ArrayList<String> storiesList;
    Context ctx;

    public HomeStoryAdapter(Context context,ArrayList<String> storiesList) {
        this.ctx = context;
        this.storiesList=storiesList;

    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itam_home_story, parent, false);
        return new VideoInfoHolder(itemView);
    }
    private  RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop().error(R.mipmap.robokart_logo);
    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {

        Uri uri=Uri.parse(storiesList.get(position));

        Glide.with(ctx).load(uri).apply(requestOptions).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.story_thumb);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.container.setVisibility(View.VISIBLE);
                Bundle bundle=new Bundle();
                bundle.putString("id",""+position );
                StoriesFragment storiesFragment=new StoriesFragment();
                storiesFragment.setArguments(bundle);
                loadFragment(storiesFragment);
            }
        });

    }


    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder {

        CircleImageView story_thumb;
        RelativeLayout relativeLayout;
        public VideoInfoHolder(View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.relativeLayout_story);
            story_thumb=itemView.findViewById(R.id.story_thumbnail);
        }

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = ((HomeActivity)ctx).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}