package com.ark.robokart_robotics.Activities.Home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.NotifyAct;
import com.ark.robokart_robotics.Activities.Profile.NewProfileAct;
import com.ark.robokart_robotics.Activities.Profile.ProfileActivity;
import com.ark.robokart_robotics.Activities.Refer.ReferActivity;
import com.ark.robokart_robotics.Activities.terms.TermsActivity;
import com.ark.robokart_robotics.Adapters.SlidingImage_Adapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.Fragments.AskDoubt.AskDoubtFragment;
import com.ark.robokart_robotics.Fragments.Courses.CoursesFragment;
import com.ark.robokart_robotics.Fragments.Dashboard.New_Dashboard;
import com.ark.robokart_robotics.Fragments.Stories.StoriesFragment;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import carbon.widget.Button;
import carbon.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity {

    private SlidingRootNav slidingRootNav;

    private RelativeLayout trans_layout;

    private ImageView drawer_btn, back_arrow;

    private TextView name,version;

    private android.widget.TextView tvGood, tvName;

    FragmentManager fragmentManager;

    public static View frmCont;

    private final int drawerOpened = 0;

    LinearLayout llDashboard, llCourse, llSupport;

    ImageView imgdashboard,imgCourses, imgsupport;

    Handler mHandler;

    public static FragmentTransaction ft;

    public static Context mContext;

    public Fragment fragment;

     LinearLayout profile_linear,rate_linear,courses,terms,support,share_linear,notify_linear;

    private Button logout_btn;

    private CircleImageView profile_image;

    SharedPref sharedPref;
    String user_id,version_server;
    String customer_email;
    public static String invited_users;

    //GoogleSignInClient mGoogleSignInClient;

public static String img_url;

    private RequestQueue requestQueue,requestQueue2,requestQueue3;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    //private static final String[] IMAGES= {""};
    private final Vector<String> ImagesArray = new Vector<>();
    public static FragmentManager fm;
    public static BottomNavigationView bottomNavigationView;
    String mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //startActivity(new Intent(this, TestDevaAct.class));

        frmCont=findViewById(R.id.frame_container);
        fm = getSupportFragmentManager();
        ft=fm.beginTransaction();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        init(savedInstanceState);

        listeners();
        setBottomNav();

        gotDynamicLink();

        getInvitedUsers();

        //RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        //RateThisApp.showRateDialogIfNeeded(this);

   /*     RateThisApp.Config config = new RateThisApp.Config(0,2);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        //config.setCancelButtonText(R.string.my_own_cancel);
        RateThisApp.init(config);

*/

        Intent bundle=getIntent();

        if(getIntent().hasExtra("payment")){
            if(bundle.getStringExtra("payment").equals("ok"))
                openCourses();
        }
        if(getIntent().hasExtra("post")){
            if(bundle.getStringExtra("post").equals("ok"))
                openAskDoubt();
            else if(bundle.getStringExtra("post").equals("story"))
                openStory();

            Log.i("HomePost","came:"+bundle.getStringExtra("post"));
            bundle.removeExtra("post");
        }else if(getIntent().hasExtra("story")){
            if(bundle.getStringExtra("story").equals("go"))
                openStory();

            Log.i("HomeStory","came:"+bundle.getStringExtra("story"));
            bundle.removeExtra("story");
        }


        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",Context.MODE_PRIVATE);
        mail = sharedPreferences.getString("stud_number","Def9657048200");
        user_id = sharedPreferences.getString("customer_id","848");
        //Toast.makeText(mContext, "Mail: "+mail, Toast.LENGTH_SHORT).show();

        FirebaseMessaging.getInstance().subscribeToTopic("RBK"+mail);
        FirebaseMessaging.getInstance().subscribeToTopic("Robokart");
        Log.i("topic subscribe Home","robokart");
        
        checkUpdate();

    }


    private void gotDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new com.google.android.gms.tasks.OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String[] arr=deepLink.toString().split("=");
                            String refer_id=arr[1];
                            Log.i("Refer Link","referID:"+refer_id);
                            sendReferToServer(refer_id);
                            //Toast.makeText(HomeActivity.this, "linkis: "+deepLink, Toast.LENGTH_SHORT).show();
                        }

                    }
                })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "getDynamicLink:onFailure", e);
            }
        });
    }

    private void sendReferToServer(String refer_id) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.send_refer_info, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                Log.i("SendRefer Respo",response);
                JSONObject refer = jsonObject.getJSONObject("refer");
                int status = refer.getInt("status");
                String msg = refer.getString("message");
                //Toast.makeText(context, ""+response, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                if (status == 1) {

                }else if (status == 0) {

                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                //Log.d(TAG, "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("User_id",user_id);
                parameters.put("refered_by",refer_id);
                return parameters;
            }
        };
        requestQueue2.add(request);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {

            }
        });
    }

    private void showReview() {
        Activity activity=this;
        ReviewManager manager = ReviewManagerFactory.create(activity);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                    flow.addOnCompleteListener(task2 -> {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Log.i("activity", "In-app review returned."+reviewInfo);
                    });
                } else {
                    // There was some problem, continue regardless of the result.
                    Toast.makeText(activity, "problem in review", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Log.i("activity", "Exception from openReview():"+ex);
            }
        });

    }

    private void checkUpdate() {
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(HomeActivity.this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
Log.i("HomeAct app update",""+appUpdateInfoTask);
// Checks whether the platform allows the specified type of update,
// and checks the update priority.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                Log.i("HomeAct app update",""+result);
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request an immediate update.
                    Log.d("Home ACt", "Update available");
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                result,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                AppUpdateType.IMMEDIATE,
                                // The current activity making the update request.
                                HomeActivity.this,
                                // Include a request code to later monitor this update request.
                                143);
                    }catch (Exception e){
                        Log.e("error in update",e.getMessage());
                    }
                }else
                    Log.i("Home Act","No update found!");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 143){
            if (requestCode != RESULT_OK){
                Log.i("Update flow failed!" ,""+ resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("customer_image","https://img.icons8.com/officel/2x/user.png");
        user_id = sharedPreferences.getString("customer_id","");
        Glide.with(getApplicationContext()).load(url).disallowHardwareConfig().into(profile_image);
        Log.i("img url",url);
        //Toast.makeText(mContext, "url: "+url, Toast.LENGTH_SHORT).show();
    }
public static FrameLayout container;
    public void init(Bundle savedInstanceState){

        container=findViewById(R.id.frame_container);
        container.setVisibility(View.GONE);
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

/*        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

*/

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

        courses=findViewById(R.id.courses_linear);
        terms=findViewById(R.id.terms);
        support=findViewById(R.id.support);

        profile_linear = findViewById(R.id.profile_linear);
        rate_linear = findViewById(R.id.rate_linear);
        share_linear=findViewById(R.id.app_share_linear);
        notify_linear=findViewById(R.id.notify_linear);
        version=findViewById(R.id.menu_version);

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            String ver = pInfo.versionName;
            version.setText("Version \n "+ver);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //for(int i=0;i<IMAGES.length;i++)
          //  ImagesArray.add(IMAGES[i]);

        mPager = findViewById(R.id.pager);

getBanner();
//getVersion();
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
               // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
               // mTextField.setText("done!");
                showReview();
            }
        }.start();

/*
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainFrameLayout, new New_Dashboard());
        fragmentTransaction.commit();
*/
        setFragment("dashboard");

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            SharedPreferences sharedPreferences1 = getSharedPreferences("userdetails",MODE_PRIVATE);
            String url = sharedPreferences1.getString("customer_image","https://img.icons8.com/officel/2x/user.png");
            Glide.with(getApplicationContext()).load(Uri.parse(url).toString().trim()).into(profile_image);
            name.setText(sharedPreferences.getString("fullname","-"));

            String fullname = sharedPreferences.getString("fullname","-");
            customer_email = sharedPreferences.getString("customer_email","");

            String image = sharedPreferences.getString("customer_image","");

            Glide.with(getApplicationContext()).load(image).into(profile_image);
            //Toast.makeText(mContext, "img link: "+image, Toast.LENGTH_SHORT).show();
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
if(slidingRootNav.isMenuClosed()) {
    slidingRootNav.openMenu();
    trans_layout.setVisibility(View.VISIBLE);
}else{
    slidingRootNav.closeMenu();
    //v.setVisibility(View.GONE);
}
            }
        });


        trans_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slidingRootNav.isMenuOpened()) {
                    slidingRootNav.closeMenu();
                    v.setVisibility(View.GONE);
                }else{
                    slidingRootNav.openMenu();
                    //trans_layout.setVisibility(View.VISIBLE);
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
                                   // mGoogleSignInClient.signOut();
                                    finish();
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Robokart");
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("RBK"+mail);
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

/*
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
*/
        profile_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        startActivity(new Intent(getApplicationContext(), NewProfileAct.class));
                    }
                },100);

            }
        });
        rate_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getApplication().getPackageName())));
            }
        });

        share_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                startActivity(new Intent(HomeActivity.this, ReferActivity.class));
                /*
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage= "\nRobokart app रोबोटिक्स हो या कोडिंग सब कुछ इतने मजे से सीखते है कि एक बार में सब दिमाग में.. \n" +
                            "खुद ही देखलो.... मान जाओगे \uD83D\uDE07\n\n";
                    shareMessage = shareMessage + "https://robokart.com/app/share";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));
                } catch(Exception e) {
                    //e.toString();
                }
                */
            }
        });
        notify_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();
                Intent intent=new Intent(HomeActivity.this, NotifyAct.class);
                startActivity(intent);
            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                Intent intent=new Intent(HomeActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                imgCourses.setBackground(getResources().getDrawable(R.drawable.ic_course_btn_selected));
                imgdashboard.setBackground(getResources().getDrawable(R.drawable.dashboard_deselected));
                imgsupport.setImageDrawable(getResources().getDrawable(R.drawable.technical_support));

                fragment = new CoursesFragment();
                setFragment(fragment);
            }
        });
    }

    private void setBottomNav() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.dashboard:

                    fragment = new New_Dashboard();
                    setFragment(fragment);
                    return true;
                case R.id.courses:

                    fragment = new CoursesFragment();
                    setFragment(fragment);
                    return true;
                case R.id.stories:

                    fragment = new StoriesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.ask_doubt:

                    fragment = new AskDoubtFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        });
    }

    public void setFragment(String screen){
        findViewById(R.id.frame_container).setVisibility(View.GONE);
        if(screen.equals("courses")){
            imgCourses.setBackground(getResources().getDrawable(R.drawable.ic_course_btn_selected));
            imgdashboard.setBackground(getResources().getDrawable(R.drawable.dashboard_deselected));
            imgsupport.setImageDrawable(getResources().getDrawable(R.drawable.technical_support));

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

    }

    public static void setFragment( Fragment fragment) {
        container.setVisibility(View.GONE);
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
            Log.d("fragment err",e.getMessage());
            Toast.makeText(mContext, "Memory management error!", Toast.LENGTH_LONG).show();
        }

    }
    private void loadFragment(Fragment fragment) {
        findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
        Fragment f = fm.findFragmentById(R.id.mainFrameLayout);
        Fragment f2 = fm.findFragmentById(R.id.frame_container);
         if(f2 instanceof StoriesFragment && container.getVisibility()==View.VISIBLE) {
             //finish();
             //startActivity(new Intent(HomeActivity.this,HomeActivity.class));
             setFragment(new New_Dashboard());

        }
        else if(f2 instanceof AskDoubtFragment && container.getVisibility()==View.VISIBLE) {
             //finish();

             Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
             //startActivity(intent);
             setFragment(new New_Dashboard());


        }
        else if(f instanceof New_Dashboard)
            makeDialog();
        else if(f instanceof CoursesFragment)
            fm.beginTransaction().replace(R.id.mainFrameLayout,new New_Dashboard()).commit();
        else
            super.onBackPressed();
    }

    private void makeDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                    }
                })
                .show();
    }

    private void getBanner() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.banner_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("banner");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                //Toast.makeText(context, ""+response, Toast.LENGTH_LONG).show();
                ImagesArray.clear();
                if (status == 1) {
                    try{
                        for(int i = 0; i< atl.length();i++){
                            ImagesArray.add(i,""+atl.getString(i));
                            //break;
                        }
                    }
                    catch (Exception e){
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                //Log.d(TAG, "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        });
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                mPager.setAdapter(new SlidingImage_Adapter(getApplicationContext(), ImagesArray));

                NUM_PAGES =ImagesArray.size();

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
            }
        });

    }

    public void openCourses(){

        fragment = new CoursesFragment();
        setFragment(fragment);
    }
    public void openStory(){
        findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
        fragment = new StoriesFragment();
        loadFragment(fragment);
    }
    public void openAskDoubt(){
        container.setVisibility(View.VISIBLE);
        fragment = new AskDoubtFragment();
        loadFragment(fragment);
    }
    void getInvitedUsers(){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.get_invited_users, response -> {
            try {
                Log.d("HomeAct INvited",response);
                JSONObject jsonObject = new JSONObject(response);
                 invited_users= jsonObject.getString("invited_users");

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("User_id",user_id);
                return parameters;
            }
        };
        requestQueue3.add(request);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
