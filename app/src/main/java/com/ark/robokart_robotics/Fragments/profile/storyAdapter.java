package com.ark.robokart_robotics.Fragments.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ark.robokart_robotics.Activities.Story.SingleStoryActivity;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class storyAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<DoubtItem> storyItems;

    // Constructor
    public storyAdapter(Context c,ArrayList<DoubtItem> storyItems) {

        mContext = c;
        this.storyItems=storyItems;
    }

    public int getCount() {
        return storyItems.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    private RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop().error(R.mipmap.robokart_logo);
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 330);
            layoutParams.setMargins(15, 10, 15, 5);
            imageView.setLayoutParams(layoutParams);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(R.drawable.btn_bg_comman);
        Glide.with(mContext).load(storyItems.get(position).getUrl()).apply(requestOptions).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Clicked:"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, SingleStoryActivity.class);
                intent.putExtra("from","profile");
                intent.putExtra("post_id",""+storyItems.get(position).getId());

                ((Activity) mContext).startActivity(intent);
            }
        });
        return imageView;
    }

}
