package com.ark.robokart_robotics.Adapters;

import android.view.View;
import android.widget.TextView;

import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ChapterContentViewHolder extends ChildViewHolder {

    private TextView chapter_content;
    private TextView video_mins;


    public ChapterContentViewHolder(View itemView) {
        super(itemView);
        chapter_content = itemView.findViewById(R.id.chapter_content);
        video_mins = itemView.findViewById(R.id.video_mins);
    }

    public void bind(ChapterContent chptrContent){
        chapter_content.setText(chptrContent.chapter_content);
        video_mins.setText(chptrContent.video_time);
    }
}