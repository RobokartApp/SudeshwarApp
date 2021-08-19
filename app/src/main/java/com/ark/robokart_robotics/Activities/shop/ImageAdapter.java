package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<String> sliderImageId;

    ImageAdapter(Context context,ArrayList<String> sliderImageId) {
        this.mContext = context;
        this.sliderImageId=sliderImageId;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(20,20,20,20);

        String[] sArr=sliderImageId.get(position).split("/");
        //"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
        String link="https://robokart.com/app_robo.png";

        if(sArr[2].equalsIgnoreCase("drive.google.com")) {
            String img_id=sArr[5];
            link = "https://drive.google.com/uc?id=" + img_id;
        }
        else
            link=sliderImageId.get(position);
        Glide.with(mContext).load(link).into(imageView);

        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return sliderImageId.size();
    }
}
