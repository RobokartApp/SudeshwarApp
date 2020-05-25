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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.CourseDetails.CourseDetailsActivity;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends ListAdapter<CourseListModel,CourseListAdapter.CustomHolder> implements Filterable {


    private List<CourseListModel> mcourseList;
    private Context mContext;

    public CourseListAdapter(Context context, List<CourseListModel> courseListModelList) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mcourseList = courseListModelList;
    }

    private static final DiffUtil.ItemCallback<CourseListModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<CourseListModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CourseListModel oldItem, @NonNull CourseListModel newItem) {
            return oldItem.getCourse_id() == newItem.getCourse_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseListModel oldItem, @NonNull CourseListModel newItem) {
            return oldItem.getCourse_name().equals(newItem.getCourse_name());
        }
    };

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video_item,parent,false);
        return new CustomHolder(itemView);
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class CustomHolder extends RecyclerView.ViewHolder{
        ImageView ivVideo, overlay;
        TextView tvVideoName;
        TextView tvPeople,tvRating;
        RelativeLayout video_relative;

        public CustomHolder(@NonNull View itemView) {
            super(itemView);

            ivVideo = itemView.findViewById(R.id.ivVideo);
            overlay = itemView.findViewById(R.id.overlay);
            tvVideoName = itemView.findViewById(R.id.tvVideoName);
            tvPeople = itemView.findViewById(R.id.tvPeople);
            tvRating = itemView.findViewById(R.id.tvRating);
            video_relative = itemView.findViewById(R.id.video_relative);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        CourseListModel answers = mcourseList.get(position);
        holder.tvVideoName.setText(String.valueOf(answers.getCourse_name()));
        holder.tvPeople.setText(answers.getCourse_enrolled());
        holder.tvRating.setText(answers.getCustomer_rating());

        Glide.with(mContext).load(answers.getCourse_video_thumb()).disallowHardwareConfig().into(holder.ivVideo);

        holder.video_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("courseid",answers.getCourse_id());
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


    public void filterList(ArrayList<CourseListModel> filteredList) {
        mcourseList = filteredList;
        notifyDataSetChanged();
    }


}

