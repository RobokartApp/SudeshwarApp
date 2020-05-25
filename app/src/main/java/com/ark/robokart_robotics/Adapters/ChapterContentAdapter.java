package com.ark.robokart_robotics.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.ChapterName;
import com.ark.robokart_robotics.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ChapterContentAdapter extends ExpandableRecyclerViewAdapter<ChapterNameViewHolder, ChapterContentViewHolder>{


    public ChapterContentAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ChapterNameViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chapter_name_item,parent,false);
        return new ChapterNameViewHolder(v);
    }

    @Override
    public ChapterContentViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chapter_content_item,parent,false);
        return new ChapterContentViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(ChapterContentViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ChapterContent chapterContent = (ChapterContent) group.getItems().get(childIndex);
        holder.bind(chapterContent);
    }

    @Override
    public void onBindGroupViewHolder(ChapterNameViewHolder holder, int flatPosition, ExpandableGroup group) {
        final ChapterName chapterName = (ChapterName) group;
        holder.bind(chapterName);
    }

}
