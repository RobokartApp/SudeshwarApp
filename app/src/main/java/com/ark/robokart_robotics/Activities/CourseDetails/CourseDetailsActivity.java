package com.ark.robokart_robotics.Activities.CourseDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.Activities.View_all_search.ViewAllSearchViewModel;
import com.ark.robokart_robotics.Adapters.CourseInclusionAdapter;
import com.ark.robokart_robotics.Adapters.CourseListAdapter;
import com.ark.robokart_robotics.Adapters.CustomAdapter;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardViewModel;
import com.ark.robokart_robotics.Fragments.Payment.BillingDetailsFragment;
import com.ark.robokart_robotics.Fragments.Payment.BuyNowFragment;
import com.ark.robokart_robotics.Model.CourseInclusionModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;
import com.example.vimeoplayer2.UniversalMediaController;
import com.example.vimeoplayer2.UniversalVideoView;
import com.example.vimeoplayer2.vimeoextractor.OnVimeoExtractionListener;
import com.example.vimeoplayer2.vimeoextractor.VimeoExtractor;
import com.example.vimeoplayer2.vimeoextractor.VimeoVideo;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

import carbon.widget.Button;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CourseDetailsActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback, PaymentResultListener, BillingDetailsFragment.onClickListener {

    private static final String TAG = "MainActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private static final String VIMEO_VIDEO_URL = "https://player.vimeo.com/video/354370082";

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    Checkout checkout;


    ImageView back_btn, play_btn, play_quiz_challenge;

    RecyclerView courseInclusionRecyclerview, alsoviewedRecyclerview;

    public CourseInclusionAdapter courseInclusionAdapter;

    public CourseInclusionViewModel courseInclusionViewModel;

    public ViewAllSearchViewModel viewAllSearchViewModel;

    public CustomAdapter customAdapter;

    public LinearLayout course_details_section;

    public Button enroll_now;

    public FrameLayout paymentFragment;

    private boolean isOpen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Checkout.preload(getApplicationContext());
        checkout = new Checkout();

        init();

        listeners();


        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });

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

    }


    //Initialise UI
    public void init(){
        play_btn = findViewById(R.id.center_play_btn);
        mVideoLayout = findViewById(R.id.video_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        enroll_now = findViewById(R.id.enroll_now);
        course_details_section = findViewById(R.id.course_details_section);
        paymentFragment = findViewById(R.id.paymentFragment);
        play_quiz_challenge = findViewById(R.id.play_quiz_challenge);

        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
        mVideoView.seekTo(mSeekPosition);
        mMediaController.setTitle("ROS basics: Program Robots");

        back_btn = findViewById(R.id.back_btn);

        courseInclusionRecyclerview = findViewById(R.id.courseInclusionRecyclerview);

        alsoviewedRecyclerview = findViewById(R.id.alsoviewedRecyclerview);


        courseInclusionViewModel = new ViewModelProvider(this).get(CourseInclusionViewModel.class);

        viewAllSearchViewModel = new ViewModelProvider(this).get(ViewAllSearchViewModel.class);
    }


    //Event Listeners
    public void listeners(){
        courseInclusionViewModel.getCourseInclusionList().observe(this, new Observer<List<CourseInclusionModel>>() {
            @Override
            public void onChanged(List<CourseInclusionModel> recommendationsList) {
                prepareRecyclerView(recommendationsList);
            }
        });


//        viewAllSearchViewModel.getCourseList().observe(this, new Observer<List<CourseListModel>>() {
//            @Override
//            public void onChanged(List<CourseListModel> courseListModels) {
//                prepareViewedRecyclerView(courseListModels);
//            }
//        });


        enroll_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                paymentFragment.setVisibility(View.VISIBLE);
                course_details_section.setVisibility(View.GONE);

                BuyNowFragment buyNowFragment = new BuyNowFragment();

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
}
