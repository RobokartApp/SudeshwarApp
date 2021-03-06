package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.robokart_robotics.Model.ChapterName;
import com.ark.robokart_robotics.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;


public class ChapterNameViewHolder extends GroupViewHolder {

    private final TextView chapter_name;
    private final ImageView arrow;
    private final Context mContext;

    public ChapterNameViewHolder(Context context,View itemView) {
        super(itemView);
        chapter_name = itemView.findViewById(R.id.chapter_name);
        arrow = itemView.findViewById(R.id.arrow);
        this.mContext = context;
    }

    public void bind(ChapterName chptrName){
        chapter_name.setText(chptrName.getTitle());

        String chp_name = chapter_name.getText().toString();

        if(chp_name.contains("Quiz") || chp_name.contains("Test")) {
            arrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.purple_arrow));
        }
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(false);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
