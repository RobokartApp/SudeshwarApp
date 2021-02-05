package com.ark.robokart_robotics.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ark.robokart_robotics.Fragments.Atl.AtlQuizFragment;
import com.ark.robokart_robotics.Fragments.Atl.CircuitFragment;
import com.ark.robokart_robotics.Fragments.Atl.CodeFragment;
import com.ark.robokart_robotics.Fragments.Atl.ComponentsFrag;
import com.ark.robokart_robotics.Fragments.Atl.OutputFragment;
import com.ark.robokart_robotics.Fragments.Atl.ProbStatFrag;
import com.ark.robokart_robotics.Fragments.Atl.ProcedureFrag;
import com.google.android.material.tabs.TabLayout;

public class MyAdapter extends FragmentPagerAdapter {

    private final Context myContext;
    int totalTabs;
    TabLayout tabLayout;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs, TabLayout tabLayout) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.tabLayout=tabLayout;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (tabLayout.getTabAt(position).getText().toString()) {
            case "Problem Statement":
                ProbStatFrag homeFragment = new ProbStatFrag();
                return homeFragment;
            case "Components":
                ComponentsFrag sportFragment = new ComponentsFrag();
                return sportFragment;
            case "Procedure":
                ProcedureFrag movieFragment = new ProcedureFrag();
                return movieFragment;
            case "Circuit":
                CircuitFragment coursef = new CircuitFragment();
                return coursef;
            case "Code":
                CodeFragment code = new CodeFragment();
                return code;
            case "Quiz":
                AtlQuizFragment atlf = new AtlQuizFragment();
                return atlf;
            case "Output":
                OutputFragment output = new OutputFragment();
                return output;
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
