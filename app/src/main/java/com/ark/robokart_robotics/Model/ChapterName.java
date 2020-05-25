package com.ark.robokart_robotics.Model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ChapterName extends ExpandableGroup<ChapterContent> {

    public ChapterName(String title, List<ChapterContent> items) {
        super(title, items);
    }
}
