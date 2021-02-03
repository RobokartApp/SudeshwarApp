package com.ark.robokart_robotics.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.ark.robokart_robotics.Activities.FreeCourses.YouTube;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.Quiz.DailyQuizActivity;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardFragment;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.util.Vector;


public class SlidingImage_Adapter extends PagerAdapter {


    private final Vector<String> IMAGES;
    private final LayoutInflater inflater;
    private final Context context;


    public SlidingImage_Adapter(Context context, Vector<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.banner_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.banner_img);
if(position==0){
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "First clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context, YouTube.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    });
}else if(position==1 || position==3){
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DashboardFragment uploadDoc= new DashboardFragment();
            HomeActivity.fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim)
                    .replace(R.id.mainFrameLayout, uploadDoc, "mycourses")
                    .addToBackStack(null)
                    .commit();
        }
    });
}else if(position==2){
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DailyQuizActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    });
}

       // imageView.setImageResource();
        Glide.with(context).load(IMAGES.elementAt(position))
                .into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
