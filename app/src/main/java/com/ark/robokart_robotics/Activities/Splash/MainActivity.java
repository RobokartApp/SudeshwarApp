package com.ark.robokart_robotics.Activities.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.AskDoubt.SinglePostActivity;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.Login.LoginActivity;
import com.ark.robokart_robotics.Activities.OnBoarding.OnBoardingActivity;
import com.ark.robokart_robotics.Activities.Story.SingleStoryActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    private RequestQueue requestQueue;
    Handler mHandler;
    public static Vector<String> vidId=new Vector<>();
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        logo = findViewById(R.id.logo);

        FrameLayout mainFrame = findViewById(R.id.framLaout);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.hyperspace_jump);
        mainFrame.startAnimation(hyperspaceJumpAnimation);
        getIds();
        //getBanner();
        new CountDownTimer(2700,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                onLoadingDataEnded();
            }
        }.start();

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","848");

        //gotDynamicLink();
/*
        logo.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                onLoadingDataEnded();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });*/

        mHandler = new Handler();

//        startLoadingData();

    }


    private void startLoadingData() {
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadingDataEnded();
            }
        }, 1000 + random.nextInt(10000));
    }


    private void onLoadingDataEnded() {


//        SharedPreferences sharedPreferences = getSharedPreferences("login_details", Context.MODE_PRIVATE);
//        int status = sharedPreferences.getInt("status", 0);
//
//        if (status==0) {
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        }
//        else {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
//        finish();

        SharedPref sharedPref = new SharedPref();

        int val = sharedPref.getOnboardingStatus(getApplicationContext());

        int status_val = sharedPref.checkLoginStatus(getApplicationContext());


        int status_standard = sharedPref.checkStandardStatus(getApplicationContext());

        String story_id;
        Intent mainIntent=getIntent();

        Intent homeIntent=new Intent(getApplicationContext(),HomeActivity.class);
        //Intent homeIntent=new Intent(getApplicationContext(), TestTryActivity.class);

        if(getIntent().hasExtra("story")){
            if(getIntent().getStringExtra("story").equals("go"))
                homeIntent.putExtra("post","story");

            Log.i("MainAct","came:"+getIntent().getStringExtra("story"));
        }

        if(mainIntent.getData()!=null && (mainIntent.getData().getScheme().equals("robokartstory"))){
            Uri data = mainIntent.getData();
            Log.i("MainAct rbk_story","data: "+data);
            //Toast.makeText(this, "Data is here RobokartStory", Toast.LENGTH_SHORT).show();

            String post_id="";
            List<String> pathSegments = data.getPathSegments();
            if(pathSegments.size()>0)
                post_id=pathSegments.get(0);
            Intent intent = new Intent(getApplicationContext(), SingleStoryActivity.class);

            intent.putExtra("post_id",""+post_id);

            Log.i("MainAct","post_id:"+post_id);
            startActivity(intent);
            finish();
        }else
        if(mainIntent.getData()!=null && (mainIntent.getData().getScheme().equals("robokartdoubt"))){
            Uri data = mainIntent.getData();
            Log.i("MainAct rbk_doubt","data: "+data);
            //Toast.makeText(this, "Data is here RobokartStory", Toast.LENGTH_SHORT).show();

            String post_id="";
            List<String> pathSegments = data.getPathSegments();
            if(pathSegments.size()>0)
                post_id=pathSegments.get(0);
            Intent intent = new Intent(getApplicationContext(), SinglePostActivity.class);

            intent.putExtra("post_id",""+post_id);

            Log.i("MainAct","post_id:"+post_id);
            startActivity(intent);
            finish();

        }else{
            Log.i("MainAct","No POst & No Story:");

            if(status_val == 1){
                if(val == 0){
                    Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
            else{
                if(val == 0){
                    Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }



    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    public void getIds(){
        StringRequest request = new StringRequest(Request.Method.GET, ApiConstants.HOST + ApiConstants.youtube_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("youtube");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                //Toast.makeText(MainActivity.this, ""+atl.getString(0), Toast.LENGTH_LONG).show();
                vidId.clear();
                if (status == 1) {
                    try{
                        for(int i = 0; i< atl.length();i++){

                            vidId.add(i,""+atl.getString(i));

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                return parameters;
            }
        };
        requestQueue.add(request);
    }


}
