package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingActivity;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.CurriculumModel;
import com.ark.robokart_robotics.R;

import java.util.List;

public class CurriculumAdapter extends ListAdapter<CourseListModel,CurriculumAdapter.CurriculumHolder> {

    private List<CurriculumModel> mcourseList;
    private Context mContext;

    public CurriculumAdapter(Context context, List<CurriculumModel> courseListModelList) {
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
    public CurriculumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_curriculum_item,parent,false);
        return new CurriculumHolder(itemView);
    }


    public class CurriculumHolder extends RecyclerView.ViewHolder{
        ImageView ivVideo, overlay;
        TextView curriculum_file_name;
        TextView tvPeople,tvRating;
        RelativeLayout video_relative;

        public CurriculumHolder(@NonNull View itemView) {
            super(itemView);

//            ivVideo = itemView.findViewById(R.id.ivVideo);
//            overlay = itemView.findViewById(R.id.overlay);
              curriculum_file_name = itemView.findViewById(R.id.curriculum_file_name);
//            tvPeople = itemView.findViewById(R.id.tvPeople);
//            tvRating = itemView.findViewById(R.id.tvRating);
//            video_relative = itemView.findViewById(R.id.video_relative);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull CurriculumHolder holder, int position) {
        CurriculumModel curriculumModel = mcourseList.get(position);

        holder.curriculum_file_name.setText(curriculumModel.getCurr_name());
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        if (mcourseList != null && mcourseList.size() > 0) {
            return mcourseList.size();
        } else {
            return 0;
        }
    }





}
