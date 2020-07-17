package com.ark.robokart_robotics.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.Profile.ProfileActivity;
import com.ark.robokart_robotics.Adapters.SlidingImage_Adapter;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.Fragments.Courses.CoursesFragment;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardFragment;
import com.ark.robokart_robotics.Fragments.Dashboard.New_Dashboard;
import com.ark.robokart_robotics.Fragments.Support.TechnicalSupport;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import carbon.widget.Button;
import carbon.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private SlidingRootNav slidingRootNav;

    private RelativeLayout trans_layout;

    private ImageView drawer_btn, back_arrow;

    private TextView name;

    private android.widget.TextView tvGood, tvName;

    FragmentManager fragmentManager;

    private int drawerOpened = 0;

    LinearLayout llDashboard, llCourse, llSupport;

    ImageView imgdashboard,imgCourses, imgsupport;

    Handler mHandler;

    public static Context mContext;

    public Fragment fragment;

    public LinearLayout profile_linear;

    private Button logout_btn;

    private CircleImageView profile_image;

    SharedPref sharedPref;

    GoogleSignInClient mGoogleSignInClient;


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.mipmap.banner660,R.drawable.banner_top,R.mipmap.banner660};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init(savedInstanceState);

        listeners();



    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("URL",Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("image_url","https://img.icons8.com/officel/2x/user.png");
        //Glide.with(getApplicationContext()).load(Uri.parse(url).toString().trim()).disallowHardwareConfig().into(profile_image);
    }

    public void init(Bundle savedInstanceState){

        mContext = HomeActivity.this;

        drawer_btn = findViewById(R.id.drawer_btn);

        trans_layout = findViewById(R.id.trans_layout);

        llCourse = findViewById(R.id.llCourse);

        llDashboard = findViewById(R.id.llDashboard);

        llSupport = findViewById(R.id.llSupport);

        imgdashboard = findViewById(R.id.imgdashboard);

        imgCourses = findViewById(R.id.imgcourses);

        imgsupport = findViewById(R.id.imgsupport);

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

        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = findViewById(R.id.pager);



        mPager.setAdapter(new SlidingImage_Adapter(getApplicationContext(),ImagesArray));


        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);

        setFragment("dashboard");

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            SharedPreferences sharedPreferences1 = getSharedPreferences("URL",MODE_PRIVATE);
            String url = sharedPreferences1.getString("image_url","https://img.icons8.com/officel/2x/user.png");
            Glide.with(getApplicationContext()).load(Uri.parse(url).toString().trim()).disallowHardwareConfig().into(profile_image);
            name.setText(sharedPreferences.getString("fullname","-"));

            String fullname = sharedPreferences.getString("fullname","-");

            String image = sharedPreferences.getString("customer_image","");

            Glide.with(getApplicationContext()).load(image).into(profile_image);
            //Toast.makeText(mContext, ""+image, Toast.LENGTH_SHORT).show();
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
        else if(hour >= 0 && hour < 4){
            greeting = "Good Night,";
        }

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

                    slidingRootNav.openMenu();
                    trans_layout.setVisibility(View.VISIBLE);

            }
        });


        trans_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                v.setVisibility(View.GONE);
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

                        new MaterialDialog.Builder(HomeActivity.this)
                                .title("Are you sure you want to logout?")
                                .positiveText("Yes")
                                .negativeText("No")
                                .onPositive((dialog, which) -> {
                                    sharedPref = new SharedPref();
                                    sharedPref.setLoginStatus(HomeActivity.this,0);
                                    SharedPreferences sharedPreferences = getSharedPreferences("userdetails",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    mGoogleSignInClient.signOut();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                })
                                .onNegative((dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
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

        llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment("support");
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
            imgsupport.setBackground(getResources().getDrawable(R.drawable.technical_support));

            fragment = new CoursesFragment();
            setFragment(fragment);
        }
        else if(screen.equals("dashboard")){
            imgCourses.setBackground(getResources().getDrawable(R.drawable.ic_course_btn_deselected));
            imgdashboard.setBackground(getResources().getDrawable(R.drawable.dashboard_btn_active));
            imgsupport.setImageDrawable(getResources().getDrawable(R.drawable.technical_support));

            fragment = new New_Dashboard();
            setFragment(fragment);
        }

        else if(screen.equals("support")){
            imgCourses.setBackground(getResources().getDrawable(R.drawable.ic_course_btn_deselected));
            imgdashboard.setBackground(getResources().getDrawable(R.drawable.dashboard_deselected));
            imgsupport.setImageDrawable(getResources().getDrawable(R.drawable.technical_support_selected));

            fragment = new TechnicalSupport();
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
