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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingActivity;
import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.CurriculumModel;
import com.ark.robokart_robotics.R;
import com.artjimlop.altex.AltexImageDownloader;

import java.util.ArrayList;
import java.util.List;

public class CurriculumAdapter extends ListAdapter<CourseListModel,CurriculumAdapter.CurriculumHolder> {

    private List<CurriculumModel> mcourseList;
    private Context mContext;

    private OnItemClickListener listener;

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

        TextView curriculum_file_name;
        ImageView file_download;

        public CurriculumHolder(@NonNull View itemView) {
            super(itemView);

            curriculum_file_name = itemView.findViewById(R.id.curriculum_file_name);
            file_download = itemView.findViewById(R.id.file_download);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull CurriculumHolder holder, int position) {
        CurriculumModel curriculumModel = mcourseList.get(position);

        holder.curriculum_file_name.setText("Curriculum");

        String url = curriculumModel.getCurriculum_file();

        Toast.makeText(mContext, url,Toast.LENGTH_SHORT).show();

        holder.file_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AltexImageDownloader
                        .writeToDisk(mContext, url, mContext.getExternalFilesDir("Robokart/") + "Curriculum/");
            }
        });
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


    public interface OnItemClickListener{
        void onItemClick(String url);
    }

    public void setOnItemClickListener(CurriculumAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


}
