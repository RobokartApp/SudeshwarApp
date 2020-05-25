package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.CourseDetails.CourseDetailsActivity;
import com.ark.robokart_robotics.Activities.CourseEnrolled.CourseEnrolledDetailsActivity;
import com.ark.robokart_robotics.Model.MyCoursesModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesAdapter extends ListAdapter<MyCoursesModel,MyCoursesAdapter.CustomHolder> {


    private List<MyCoursesModel> mcourseList;
    private Context mContext;

    public MyCoursesAdapter(Context context, List<MyCoursesModel> courseListModelList) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mcourseList = courseListModelList;
    }

    private static final DiffUtil.ItemCallback<MyCoursesModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MyCoursesModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyCoursesModel oldItem, @NonNull MyCoursesModel newItem) {
            return oldItem.getLearn_percent() == newItem.getLearn_percent();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyCoursesModel oldItem, @NonNull MyCoursesModel newItem) {
            return oldItem.getCourse_name().equals(newItem.getCourse_name());
        }
    };

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_courses_item,parent,false);
        return new CustomHolder(itemView);
    }


    public class CustomHolder extends RecyclerView.ViewHolder{
        ImageView ivVideo, overlay;
        TextView tvVideoName,tvProgress;
        RelativeLayout video_relative;
        ProgressBar learnProgress;


        public CustomHolder(@NonNull View itemView) {
            super(itemView);

            ivVideo = itemView.findViewById(R.id.ivVideo);
            overlay = itemView.findViewById(R.id.overlay);
            tvVideoName = itemView.findViewById(R.id.tvVideoName);
            learnProgress = itemView.findViewById(R.id.learnProgress);
            video_relative = itemView.findViewById(R.id.video_relative);
            tvProgress = itemView.findViewById(R.id.tvProgress);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        MyCoursesModel answers = mcourseList.get(position);
        holder.tvVideoName.setText(String.valueOf(answers.getCourse_name()));
        holder.tvProgress.setText(answers.getLearn_percent() +"%");
        holder.learnProgress.setProgress(Integer.parseInt(answers.getLearn_percent()));

        Glide.with(mContext).load(answers.getCourse_video_thumb()).disallowHardwareConfig().into(holder.ivVideo);

        holder.video_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseEnrolledDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });




    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return mcourseList.size();
    }





}

