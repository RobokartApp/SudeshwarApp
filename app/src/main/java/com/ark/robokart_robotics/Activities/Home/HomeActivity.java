package com.ark.robokart_robotics.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ark.robokart_robotics.Activities.Profile.ProfileActivity;
import com.ark.robokart_robotics.Fragments.Courses.CoursesFragment;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardFragment;
import com.ark.robokart_robotics.R;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import carbon.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private SlidingRootNav slidingRootNav;

    private ImageView drawer_btn, back_arrow;

    private TextView name;

    FragmentManager fragmentManager;

    private int drawerOpened = 0;

    LinearLayout llDashboard, llCourse;

    ImageView imgdashboard,imgCourses;

    Handler mHandler;

    public static Context mContext;

    public Fragment fragment;

    public LinearLayout profile_linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init(savedInstanceState);

        listeners();

    }


    public void init(Bundle savedInstanceState){

        mContext = HomeActivity.this;

        drawer_btn = findViewById(R.id.drawer_btn);

        llCourse = findViewById(R.id.llCourse);

        llDashboard = findViewById(R.id.llDashboard);

        imgdashboard = findViewById(R.id.imgdashboard);

        imgCourses = findViewById(R.id.imgcourses);

        mHandler = new Handler();

        fragmentManager = getSupportFragmentManager();


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withGravity(SlideGravity.LEFT)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        name = findViewById(R.id.name);

        back_arrow = findViewById(R.id.back_arrow);

        profile_linear = findViewById(R.id.profile_linear);

        setFragment("dashboard");
    }

    public void listeners(){
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu(true);
            }
        });


        drawer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerOpened == 0){
                    drawerOpened = 1;
                    slidingRootNav.openMenu();
                }
                else{
                    drawerOpened = 0;
                    slidingRootNav.closeMenu();
                }


            }
        });

        llCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setFragment("courses");
            }
        });

        llDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setFragment("dashboard");
            }
        });

        profile_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    }
                },300);


            }
        });
    }


    public void setFragment(String screen){
        if(screen.equals("courses")){
            imgCourses.setBackground(getResources().getDrawable(R.drawable.ic_course_btn_selected));
            imgdashboard.setBackground(getResources().getDrawable(R.drawable.dashboard_deselected));

            fragment = new CoursesFragment();
            setFragment(fragment);
        }
        else if(screen.equals("dashboard")){
            imgCourses.setBackground(getResources().getDrawable(R.drawable.ic_course_btn_deselected));
            imgdashboard.setBackground(getResources().getDrawable(R.drawable.dashboard_btn_active));

            fragment = new DashboardFragment();
            setFragment(fragment);
        }
    }



    public static void setFragment( Fragment fragment) {

        try {
            FragmentManager fragmentManager = ((HomeActivity) mContext).getSupportFragmentManager();
            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {
                fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
                fragmentTransaction.commit();
            }
        }catch (Exception e){
            Toast.makeText(mContext, "Memory management error!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
