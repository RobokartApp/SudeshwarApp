package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.CourseDetails.CourseDetailsActivity;
import com.ark.robokart_robotics.Model.CourseInclusionModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;

import java.util.List;

public class CourseInclusionAdapter extends ListAdapter<CourseListModel,CourseInclusionAdapter.CustomHolder> {

    private OnItemClickListener listener;
    private List<CourseInclusionModel> mcourseList;
    private Context mContext;
    int num = 0;

    public CourseInclusionAdapter(Context context, List<CourseInclusionModel> courseListModelList) {
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
                .inflate(R.layout.row_courseinclusion_item,parent,false);
        return new CustomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        CourseInclusionModel answers = mcourseList.get(position);
        holder.video_name.setText(String.valueOf(answers.getChapter_name()));

//        for(int i =0; i < mcourseList.size()-1; i++){
//            num++;
//            holder.chapter_num.setText(String.valueOf(num));
//        }

        if(position == 0){
            holder.pb.setProgress(0,true);
            holder.playbtn.setVisibility(View.VISIBLE);
        }
        else {
            holder.pb.setProgress(0,true);
            holder.playbtn.setVisibility(View.GONE);
        }


        String quiz = holder.video_name.getText().toString();

        if(quiz.equals("Final Quiz") || quiz.equals("Final Test") || quiz.equals("Final test") || quiz.equals("Final quiz")){
            holder.pb.setVisibility(View.GONE);
        }

//        holder.video_relative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);
//            }
//        });

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



    public class CustomHolder extends RecyclerView.ViewHolder{
        ImageView ivVideo, overlay, playbtn;
        carbon.widget.TextView chapter_num, video_name, video_mins;
        RelativeLayout video_relative;
        ProgressBar pb;

        public CustomHolder(@NonNull View itemView) {
            super(itemView);

//            ivVideo = itemView.findViewById(R.id.ivVideo);
//            overlay = itemView.findViewById(R.id.overlay);
            chapter_num = itemView.findViewById(R.id.chapter_num);
            video_name = itemView.findViewById(R.id.video_name);
            video_mins = itemView.findViewById(R.id.video_mins);
            pb = itemView.findViewById(R.id.pb);
            playbtn = itemView.findViewById(R.id.playbtn);
//            video_relative = itemView.findViewById(R.id.video_relative);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(String answer);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}