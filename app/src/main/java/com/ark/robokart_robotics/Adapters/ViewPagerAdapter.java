package com.ark.robokart_robotics.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ark.robokart_robotics.Pages.LearnOnlinePage;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();//fragment array list
    private final List<String> mFragmentTitleList = new ArrayList<>();//title array list
    private final FragmentManager mFragmentManager;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        mFragmentManager = manager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    //adding fragments and title method
    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFragAtIndex(Fragment fragment, int position) {
        mFragmentList.add(position,fragment);
    }

    public void removeFragment(int position) {
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
    }

    public void replaceFragment(Fragment oldfragment, Fragment  newfragment, int position) {
        mFragmentManager.beginTransaction().remove(oldfragment).commit();
        oldfragment = new LearnOnlinePage();
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
