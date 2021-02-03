package com.ark.robokart_robotics.Activities.AskDoubt;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class addComment {
    Context context;
    String image,name,comment;
    LinearLayout forComment;
    public addComment(Context context, String image, String name, String comment, LinearLayout forComment){
        this.context=context;
        this.comment=comment;
        this.image=image;
        this.name=name;
        this.forComment=forComment;

        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout comment_layout= (RelativeLayout) vi.inflate(R.layout.item_view_comment, null);

        String imgUrl="https://robokart.com/admin/assets/uploads/images/customer/";

        CircleImageView civ_image=comment_layout.findViewById(R.id.civ_Picture);
        TextView c_name=comment_layout.findViewById(R.id.commenter_name);
        TextView c_text=comment_layout.findViewById(R.id.comment_txt);

        Glide.with(context).load(imgUrl+image).disallowHardwareConfig().into(civ_image);
        c_name.setText(name);
        c_text.setText(comment);

        forComment.addView(comment_layout);
    }
}
