package com.ark.robokart_robotics.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.Profile.ProfileActivity;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.Fragments.Courses.CoursesFragment;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardFragment;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Calendar;
import java.util.Date;

import carbon.widget.Button;
import carbon.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private SlidingRootNav slidingRootNav;

    private ImageView drawer_btn, back_arrow;

    private TextView name;

    private android.widget.TextView tvGood, tvName;

    FragmentManager fragmentManager;

    private int drawerOpened = 0;

    LinearLayout llDashboard, llCourse;

    ImageView imgdashboard,imgCourses;

    Handler mHandler;

    public static Context mContext;

    public Fragment fragment;

    public LinearLayout profile_linear;

    private Button logout_btn;

    private CircleImageView profile_image;

    SharedPref sharedPref;

    GoogleSignInClient mGoogleSignInClient;


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

        tvName = findViewById(R.id.tvName);

        tvGood = findViewById(R.id.tvGood);

        mHandler = new Handler();

        fragmentManager = getSupportFragmentManager();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withGravity(SlideGravity.LEFT)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        name = findViewById(R.id.name);

        back_arrow = findViewById(R.id.back_arrow);

        profile_image = findViewById(R.id.profile_image);

        logout_btn = findViewById(R.id.logout_btn);

        profile_linear = findViewById(R.id.profile_linear);

        setFragment("dashboard");

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            SharedPreferences sharedPreferences1 = getSharedPreferences("URL",MODE_PRIVATE);
            String url = sharedPreferences1.getString("image_url","https://img.icons8.com/officel/2x/user.png");
            Glide.with(getApplicationContext()).load(Uri.parse(url).toString().trim()).disallowHardwareConfig().into(profile_image);
            name.setText(sharedPreferences.getString("fullname","-"));

            String fullname = sharedPreferences.getString("fullname","-");

            String lastName = "";
            String firstName= "";
            if(fullname.split("\\w+").length>1){

                lastName = fullname.substring(fullname.lastIndexOf(" ")+1);
                firstName = fullname.substring(0, fullname.lastIndexOf(' '));
            }
            else{
                firstName = fullname;
            }

            tvName.setText(firstName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        //Set greeting
        String greeting = null;
        if(hour>=6 && hour<12){
            greeting = "Good Morning,";
        } else if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon,";
        } else if(hour >= 17 && hour < 24){
            greeting = "Good Evening,";
        }

//        else if(hour >= 21 && hour < 24){
//            greeting = "Good Night";
//        }

        tvGood.setText(greeting);

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

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sharedPref = new SharedPref();
                        sharedPref.setLoginStatus(HomeActivity.this,0);
                        mGoogleSignInClient.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                },300);


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
