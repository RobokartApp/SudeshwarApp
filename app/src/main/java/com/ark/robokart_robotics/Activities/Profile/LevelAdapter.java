package com.ark.robokart_robotics.Activities.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Fragments.Stories.Home_Adapter;
import com.ark.robokart_robotics.Fragments.Stories.VideoItem;
import com.ark.robokart_robotics.R;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.VideoInfoHolder> {

    //these ids are the unique id for each video
    String[] levels={"1","2",	"3"	,"4"	,"5"	,"6",	"7",	"8",	"9",	"10"};

    //int[] status={1,1,0,0,0,0,0,0,0,0};
    Context ctx;
    int active;
    private final LevelAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon,VideoInfoHolder holder, View view);
    }

    public LevelAdapter(Context context,int active,LevelAdapter.OnItemClickListener listener) {
        this.ctx = context;
        this.active=active-1;
        this.listener = listener;
       // this.VideoID=VidId;
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level_list, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final @NotNull VideoInfoHolder holder, final int position) {

        if (position==active) {
            holder.level_bg.setImageResource(R.drawable.black_gold_bg);
            holder.level.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite));
            holder.down_arrow.setVisibility(View.VISIBLE);
            holder.status_text.setText("Active");
            holder.status_text.setTextColor(Color.parseColor("#DF4001"));
            holder.level_bg.setAlpha(1f);
        }else
            if (position<active){
                holder.down_arrow.setVisibility(View.GONE);
                holder.level_bg.setImageResource(R.drawable.gold_bg_level);
                holder.status_text.setText("Completed");
                holder.status_text.setTextColor(Color.parseColor("#017C20"));
                holder.level_bg.setAlpha(1f);
            }
        else{
            holder.down_arrow.setVisibility(View.GONE);
            holder.level_bg.setImageResource(R.drawable.gold_bg_level);
            holder.level_bg.setAlpha(0.4f);
            holder.level.setAlpha(0.4f);
                holder.status_text.setText("Pending");
                holder.status_text.setTextColor(R.attr.colorSecondaryVariant);
            holder.level.setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
        }

        holder.level.setText(levels[position]);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        listener.onItemClick(position,holder,v);
    }
});

    }
    public void bind(final int position,final OnItemClickListener listener){

    }

    @Override
    public int getItemCount() {
        return levels.length;
    }

    public static class VideoInfoHolder extends RecyclerView.ViewHolder {

        public ImageView level_bg,down_arrow;
        public TextView status_text,level;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            level_bg=itemView.findViewById(R.id.level_bg_iv);
            status_text=itemView.findViewById(R.id.status_tv);
            down_arrow=itemView.findViewById(R.id.down_arrow);
            level=itemView.findViewById(R.id.level_num_tv);

        }

    }
}
