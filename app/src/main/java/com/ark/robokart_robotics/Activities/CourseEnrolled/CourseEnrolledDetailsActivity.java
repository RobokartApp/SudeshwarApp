package com.ark.robokart_robotics.Activities.CourseEnrolled;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingActivity;
import com.ark.robokart_robotics.Adapters.ChapterAdapter;
import com.ark.robokart_robotics.Adapters.ChapterContentAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.ChapterName;
import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.example.vimeoplayer2.UniversalMediaController;
import com.example.vimeoplayer2.UniversalVideoView;
import com.example.vimeoplayer2.vimeoextractor.OnVimeoExtractionListener;
import com.example.vimeoplayer2.vimeoextractor.VimeoExtractor;
import com.example.vimeoplayer2.vimeoextractor.VimeoVideo;
import com.razorpay.Checkout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseEnrolledDetailsActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback{

    private static final String TAG = "CourseEnrolledDetailsAc";

    private RecyclerView chapterContentRecyclerview;

    private CourseEnrolledDetailsViewModel courseEnrolledDetailsViewModel;

    private ChapterAdapter chapterAdapter;

    private RequestQueue requestQueue;


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

    ImageView play_quiz_challenge, video_thumb;

    ImageView back_btn, play_btn;

    public static String courseid = "";
    String customer_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_enrolled_details);

        courseEnrolledDetailsViewModel = new ViewModelProvider(this).get(CourseEnrolledDetailsViewModel.class);

        init();

        listeners();

    }

    public void init() {
        chapterContentRecyclerview = findViewById(R.id.chapterContentRecyclerview);

        play_btn = (ImageView) findViewById(R.id.center_play_btn);
        mVideoLayout = findViewById(R.id.video_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        video_thumb = findViewById(R.id.video_thumb);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);

        play_quiz_challenge = findViewById(R.id.play_quiz_challenge);
        course_name = findViewById(R.id.course_name);
        customer_rating = findViewById(R.id.customer_rating);

        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
        mVideoView.seekTo(mSeekPosition);

        back_btn = (ImageView) findViewById(R.id.back_btn);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {
            Bundle bundle = getIntent().getExtras();
            courseid = bundle.getString("courseid");
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            customer_id = sharedPreferences.getString("customer_id","0");

        } catch (Exception e) {
            e.printStackTrace();
        }

        getCourseDetails(courseid);


        courseEnrolledDetailsViewModel.getChapterName(courseid).observe(this, new Observer<List<Class_chapters>>() {
            @Override
            public void onChanged(List<Class_chapters> class_chapters) {
                chapterAdapter = new ChapterAdapter(getApplicationContext(),class_chapters);
                chapterContentRecyclerview.setAdapter(chapterAdapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        courseEnrolledDetailsViewModel.getCourseAccess(courseid,customer_id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                SharedPreferences sharedPreferences = getSharedPreferences("courseaccess",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("chapter_completed",s);
                editor.apply();
            }
        });

    }

    public void listeners() {

        video_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: touched");
                video_thumb.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                mMediaController.setVisibility(View.VISIBLE);
                mVideoView.start();

                play_btn.setVisibility(View.INVISIBLE);
                back_btn.clearAnimation();
                back_btn.setVisibility(View.INVISIBLE);

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });
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
                    String curriculum = course_details.getString("curriculum_file");

                    Log.d(TAG, "getCourseDetails: "+VIMEO_VIDEO_URL);


                    course_name.setText(c_name);

                    customer_rating.setText(cu_rating);

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
