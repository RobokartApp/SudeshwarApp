package com.ark.robokart_robotics.Activities.CourseDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.Activities.View_all_search.ViewAllSearchViewModel;
import com.ark.robokart_robotics.Adapters.CourseInclusionAdapter;
import com.ark.robokart_robotics.Adapters.CustomAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Fragments.Payment.BillingDetailsFragment;
import com.ark.robokart_robotics.Fragments.Payment.BuyNow.BuyNowFragment;
import com.ark.robokart_robotics.Model.CourseInclusionModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.example.vimeoplayer2.UniversalMediaController;
import com.example.vimeoplayer2.UniversalVideoView;
import com.example.vimeoplayer2.vimeoextractor.OnVimeoExtractionListener;
import com.example.vimeoplayer2.vimeoextractor.VimeoExtractor;
import com.example.vimeoplayer2.vimeoextractor.VimeoVideo;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.widget.Button;

public class CourseDetailsActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback, PaymentResultListener, BillingDetailsFragment.onClickListener {

    private static final String TAG = "MainActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String VIMEO_VIDEO_URL = "";

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    Checkout checkout;


    TextView course_name, customer_rating;

    ImageView  play_quiz_challenge, video_thumb;

    ImageView back_btn, play_btn;

    RecyclerView courseInclusionRecyclerview, alsoviewedRecyclerview;

    public CourseInclusionAdapter courseInclusionAdapter;

    public CourseInclusionViewModel courseInclusionViewModel;

    public ViewAllSearchViewModel viewAllSearchViewModel;

    public CustomAdapter customAdapter;

    public LinearLayout course_details_section;

    public Button enroll_now;

    public FrameLayout paymentFragment;

    private boolean isOpen = false;

    private RequestQueue requestQueue;

    String courseid = "";

    String customer_id = "";

    String online_price_title, course_online_price, offline_price_title, course_offline_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Checkout.preload(getApplicationContext());
        checkout = new Checkout();

        init();

        listeners();




    }


    //Initialise UI
    public void init(){
        play_btn = (ImageView) findViewById(R.id.center_play_btn);
        mVideoLayout = findViewById(R.id.video_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        video_thumb = findViewById(R.id.video_thumb);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        enroll_now = findViewById(R.id.enroll_now);
        course_details_section = findViewById(R.id.course_details_section);
        paymentFragment = findViewById(R.id.paymentFragment);
        play_quiz_challenge = findViewById(R.id.play_quiz_challenge);
        course_name = findViewById(R.id.course_name);
        customer_rating = findViewById(R.id.customer_rating);

        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
        mVideoView.seekTo(mSeekPosition);


        back_btn = (ImageView) findViewById(R.id.back_btn);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        courseInclusionRecyclerview = findViewById(R.id.courseInclusionRecyclerview);

        alsoviewedRecyclerview = findViewById(R.id.alsoviewedRecyclerview);


        courseInclusionViewModel = new ViewModelProvider(this).get(CourseInclusionViewModel.class);

        viewAllSearchViewModel = new ViewModelProvider(this).get(ViewAllSearchViewModel.class);

        try {
            Bundle bundle = getIntent().getExtras();
            courseid = bundle.getString("courseid");
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            customer_id = sharedPreferences.getString("customer_id","0");

        } catch (Exception e) {
            e.printStackTrace();
        }

        getCourseDetails(courseid);
    }


    //Event Listeners
    public void listeners(){
        courseInclusionViewModel.getCourseInclusionList(courseid).observe(this, new Observer<List<CourseInclusionModel>>() {
            @Override
            public void onChanged(List<CourseInclusionModel> recommendationsList) {
                prepareRecyclerView(recommendationsList);
            }
        });

        video_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: touched");
                video_thumb.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                mMediaController.setVisibility(View.VISIBLE);
                mVideoView.start();
                back_btn.setVisibility(View.INVISIBLE);
                play_btn.setVisibility(View.INVISIBLE);
            }
        });


//        viewAllSearchViewModel.getCourseList().observe(this, new Observer<List<CourseListModel>>() {
//            @Override
//            public void onChanged(List<CourseListModel> courseListModels) {
//                prepareViewedRecyclerView(courseListModels);
//            }
//        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });




        enroll_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                paymentFragment.setVisibility(View.VISIBLE);
                course_details_section.setVisibility(View.GONE);
                enroll_now.setVisibility(View.GONE);

                BuyNowFragment buyNowFragment = new BuyNowFragment();

                Bundle data = new Bundle();//Use bundle to pass data
                data.putString("home_cost", course_online_price);//put string, int, etc in bundle with a key value
                data.putString("home_desc", online_price_title);
                data.putString("makerspace_cost",course_offline_price);
                data.putString("makerspace_learn_desc",offline_price_title);
                data.putString("customer_id",customer_id);
                data.putString("course_id",courseid);
                buyNowFragment.setArguments(data);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.paymentFragment, buyNowFragment);
                fragmentTransaction.commit();

            }
        });

        play_quiz_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QuizActivity.class));
            }
        });

    }






    private void prepareRecyclerView(List<CourseInclusionModel> courseInclusionModelList) {
        courseInclusionAdapter = new CourseInclusionAdapter(getApplicationContext(),courseInclusionModelList);
        courseInclusionRecyclerview.setItemAnimator(new DefaultItemAnimator());
        courseInclusionRecyclerview.setAdapter(courseInclusionAdapter);
        courseInclusionAdapter.notifyDataSetChanged();
    }


    private void prepareViewedRecyclerView(List<CourseListModel> courseListModelList) {
        customAdapter = new CustomAdapter(getApplicationContext(),courseListModelList);
        alsoviewedRecyclerview.setItemAnimator(new DefaultItemAnimator());
        alsoviewedRecyclerview.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onPause ");
//        if (mVideoView != null && mVideoView.isPlaying()) {
//            mSeekPosition = mVideoView.getCurrentPosition();
//            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
//            mVideoView.seekTo(mSeekPosition);
//        }
//    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
              /*  mVideoView.setVideoPath(VIDEO_URL);
                mVideoView.requestFocus();*/
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
        }

        //switchTitleBar(!isFullscreen);
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }

        if(paymentFragment.getVisibility() == View.VISIBLE){
            paymentFragment.setVisibility(View.GONE);
            course_details_section.setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
        }
    }


    public void startPayment() {
        checkout.setKeyID("rzp_test_EvXZMjKBLEZ4D2");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo_wall);


        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Robokart");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");


            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", "5000");



            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(boolean isClicked) {
        if(isClicked){
            startPayment();
        }
    }


    public void getCourseDetails(String courseid){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.course_details_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");

                JSONObject course_details = result.getJSONObject("course_details");

                int status = jsonObject.getInt("statusId");

                String msg = result.getString("message");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "course: "+result.getString("message"));

                    String c_name = course_details.getString("course_name");
                    mMediaController.setTitle(c_name);
                    String cu_rating = course_details.getString("customer_rating");
                    String course_enrolled = course_details.getString("course_enrolled");
                    String course_video_thumb = course_details.getString("course_video_thumb");
                    String input = course_details.getString("course_video_url");
                    VIMEO_VIDEO_URL = input.substring(0, input.indexOf("?"));
                    online_price_title = course_details.getString("online_price_title");
                    course_online_price = course_details.getString("course_online_price");
                    offline_price_title = course_details.getString("offline_price_title");
                    course_offline_price = course_details.getString("course_offline_price");

                    course_name.setText(c_name);

                    customer_rating.setText(cu_rating);

                    enroll_now.setText("₹ "+course_online_price + " Enroll now");

                    Glide.with(getApplicationContext()).load(course_video_thumb).into(video_thumb);


                    VimeoExtractor.getInstance().fetchVideoWithURL(VIMEO_VIDEO_URL, null, new OnVimeoExtractionListener() {
                        @Override
                        public void onSuccess(final VimeoVideo video) {
                            String hdStream = null;
                            for (String key : video.getStreams().keySet()) {
                                hdStream = key;
                            }
                            final String hdStreamuRL = video.getStreams().get(hdStream);
                            if (hdStream != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Start the MediaController
                                        mVideoView.setMediaController(mMediaController);
                                        // Get the URL from String VideoURL
                                        Uri video = Uri.parse(hdStreamuRL);

                                        mVideoView.setVideoURI(video);

                                    }
                                });
                            }
                        }

//                setLink(hdStream);
                        //...
//            }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Error handling here
                        }
                    });


                }else if (status == 0) {
                    Log.d(TAG, "login: "+result.getString("message"));
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("courseid", courseid);
                return parameters;
            }
        };
        requestQueue.add(request);


    }
}
