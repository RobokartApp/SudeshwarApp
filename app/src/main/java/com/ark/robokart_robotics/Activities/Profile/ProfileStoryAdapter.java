package com.ark.robokart_robotics.Activities.Profile;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ark.robokart_robotics.Fragments.profile.DoubtFragment;
import com.ark.robokart_robotics.Fragments.profile.StoryFragment;

public class ProfileStoryAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public ProfileStoryAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StoryFragment homeFragment = new StoryFragment();
                return homeFragment;
            case 1:
                DoubtFragment sportFragment = new DoubtFragment();
                return sportFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
