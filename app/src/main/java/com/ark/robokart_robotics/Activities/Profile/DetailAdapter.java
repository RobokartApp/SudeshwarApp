package com.ark.robokart_robotics.Activities.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.R;

import org.jetbrains.annotations.NotNull;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.VideoInfoHolder> {

    //these ids are the unique id for each video
    //String[] levels={"1","2",	"3"	,"4","5"};

    String[] progress;
    Context ctx;
    String[] detail;

    public DetailAdapter(Context context,String[] detail,String[] progress) {
        this.ctx = context;
        this.detail=detail;
        this.progress=progress;
        // this.VideoID=VidId;

    }

    @Override
    public @NotNull VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_detail, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final @NotNull VideoInfoHolder holder, final int position) {


        int i=position+1;
        if (i%2==0){
            holder.level_bg.setImageResource(R.drawable.level_yellow);

        }else {
            holder.level_bg.setImageResource(R.drawable.level_purple);
        }

        holder.level_text.setText(detail[position]);
        holder.progress_text.setText(progress[position]);
        if (progress[position]!=null) {
            if (progress[position].equalsIgnoreCase("0% completed")) {
                holder.level_progress.setProgress(0);
            }
        }else
            holder.level_progress.setProgress(0);

    }

    @Override
    public int getItemCount() {
        return detail.length;
    }

    public static class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView level_bg,level_details;TextView level_text,progress_text;
        ProgressBar level_progress;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            level_bg=itemView.findViewById(R.id.level_bg_iv);
            level_details=itemView.findViewById(R.id.level_icon);
            level_text=itemView.findViewById(R.id.level_text);
            level_progress=itemView.findViewById(R.id.level_progressbar);
            progress_text=itemView.findViewById(R.id.level_progress_tv);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
