package com.ark.robokart_robotics.Fragments.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ark.robokart_robotics.Activities.AskDoubt.SinglePostActivity;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class DoubtAdapter extends BaseAdapter {
    Context context;
    ArrayList<DoubtItem> doubtItems;

    LayoutInflater inflter;
    public DoubtAdapter(Context applicationContext,ArrayList<DoubtItem> doubtItems) {
        this.context = applicationContext;
        this.doubtItems=doubtItems;

        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return doubtItems.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.doubt_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.doubt_img);
        TextView doubt=view.findViewById(R.id.doubt_text);
        icon.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        icon.setPadding(18, 8, 18, 8);// get the reference of ImageView
        String postImgUrl="https://robokart.com/app/images/posts/"+doubtItems.get(i).getUrl();

    if (doubtItems.get(i).getUrl().equals("NA"))
        icon.setImageResource(R.mipmap.robokart_logo); // set logo images
    else
        Glide.with(context).load(postImgUrl).diskCacheStrategy(DiskCacheStrategy.DATA).into(icon);

    //icon.setVisibility(View.GONE);

        doubt.setText(doubtItems.get(i).getDoubt());
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SinglePostActivity.class);
                intent.putExtra("from","profile");
                intent.putExtra("post_id",""+doubtItems.get(i).getId());

                ((Activity)context).startActivity(intent);
            }
        });
        return view;
    }

}
