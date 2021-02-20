
package com.ark.robokart_robotics.Activities.VideoPlaying;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.Adapters.CommentsAdapter;
import com.ark.robokart_robotics.Adapters.CurriculumAdapter;
import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.Model.CommentModel;
import com.ark.robokart_robotics.Model.CurriculumModel;
import com.ark.robokart_robotics.R;
import com.example.vimeoplayer2.UniversalMediaController;
import com.example.vimeoplayer2.UniversalVideoView;
import com.example.vimeoplayer2.vimeoextractor.OnVimeoExtractionListener;
import com.example.vimeoplayer2.vimeoextractor.VimeoExtractor;
import com.example.vimeoplayer2.vimeoextractor.VimeoVideo;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.model.error.VimeoError;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.Button;
import carbon.widget.TextView;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class VideoPlayingActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback{

    private static final String TAG = "VideoPlayingActivity";

    private RecyclerView curriculum_recyclerview;

    private RecyclerView comments_Recyclerview;

    private CurriculumAdapter curriculumAdapter;

    private CommentsAdapter commentsAdapter;

    private TextView chapter_name, video_time, chapter_size,short_desc;

    private EditText comment_edt;

    private LinearLayout send_comment_btn;

    private ImageView send_img;

    private ProgressBar progressBar_circular;

    private Button next_lesson_btn;

    String chapt_id;

    private ArrayList<Class_chapters.Course_List> chapterContentArrayList = new ArrayList<>();

    public VideoPlayingViewModel videoPlayingViewModel;

    public CommentModel commentModel;

    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";

    private String VIMEO_VIDEO_URL = "";

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    android.widget.TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    String min = "";

    String size = "";

    String title = "";

    String assignment = "";

    String quiz_id = "";

    String course_id = "";

    String chapt_nme = "";

    String comment = "";

    String customer_id = "";

    String total_number_chapter = "";

    String child_chpt_id = "";

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);

        init();

        listeners();

    }

    public void init(){
        //curriculum_recyclerview = findViewById(R.id.curriculum_recyclerview);

        comments_Recyclerview = findViewById(R.id.comments_Recyclerview);
short_desc=findViewById(R.id.short_desc);
        chapter_name = findViewById(R.id.chapter_name);
        video_time = findViewById(R.id.video_time);
        chapter_size = findViewById(R.id.chapter_size);

        comment_edt = findViewById(R.id.comment_edt);

        send_comment_btn = findViewById(R.id.send_comment_btn);

        next_lesson_btn = findViewById(R.id.next_lesson_btn);

        send_img = findViewById(R.id.send_img);

        progressBar_circular = findViewById(R.id.progress_circular);

        mVideoLayout = findViewById(R.id.video_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoView = findViewById(R.id.videoView);
        mMediaController = findViewById(R.id.media_controller);

        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
        mVideoView.seekTo(mSeekPosition);



        try {
            Bundle bundle = getIntent().getExtras();
            course_id = bundle.getString("course_id");
            chapt_nme = bundle.getString("chapter_name");
            chapter_name.setText(chapt_nme);

            chapt_id = String.valueOf(bundle.getInt("chpt_id"));
            chapterContentArrayList = bundle.getParcelableArrayList("courses");
            total_number_chapter = bundle.getString("total_number_chapter");
            child_chpt_id = bundle.getString("child_chpt_id");
            i=bundle.getInt("lession");
            nextLesson(i);

            Log.d(TAG, "init: "+chapterContentArrayList);


            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            customer_id = sharedPreferences.getString("customer_id","0");


            //set content
            if(chapterContentArrayList.get(0).getVideo_time().equals("null")||chapterContentArrayList.get(0).getVideo_time().equals(""))
                video_time.setVisibility(View.GONE);
            min = "Time - "+chapterContentArrayList.get(0).getVideo_time()+" mins";
            video_time.setText(min);

            size = "Lesson " +(i+1)+" / " + chapterContentArrayList.size();

            chapter_size.setText(size);

            assignment = chapterContentArrayList.get(0).getAssignment_url();
/*
            String[] sArr=assignment.split("/");
            String fileId=sArr[5];//"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
            final String DownloadUrl="https://docs.google.com/a/google.com/uc?id="+fileId;
*/
            quiz_id = chapterContentArrayList.get(0).getQuiz_id();

            short_desc.setText(chapterContentArrayList.get(i).getChapter_content());
            VIMEO_VIDEO_URL = chapterContentArrayList.get(0).getVideo_url().trim();

            if(VIMEO_VIDEO_URL.equals("") && assignment.length() > 0){
                Toast.makeText(getApplicationContext(), "Its time to do some Assignment: ",Toast.LENGTH_SHORT).show();
               // AltexImageDownloader
                 //       .writeToDisk(getApplicationContext(), assignment, getExternalFilesDir("Robokart/") + "Curriculum/");
         /*       DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(DownloadUrl);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                Long reference = downloadManager.enqueue(request);*/
                videoPlaying(VIMEO_VIDEO_URL);
            }
            else if (VIMEO_VIDEO_URL.equals("") && quiz_id.length() > 0){
                SharedPreferences quiz = getSharedPreferences("quiz_id",MODE_PRIVATE);
                SharedPreferences.Editor editor = quiz.edit();
                editor.putString("quiz_id",quiz_id);
                editor.apply();
                Log.d(TAG, "Before quiz_id: "+quiz_id);
                Toast.makeText(getApplicationContext(), "Its time for some quiz",Toast.LENGTH_SHORT).show();

                new MaterialDialog.Builder(this)
                        .title("Are you ready to play quiz ?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive((dialog, which) -> {
                            Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                            intent.putExtra("course_id",course_id);
                            intent.putExtra("chpt_id", chapt_id);
                            intent.putExtra("total_number_chapter",total_number_chapter);
                            intent.putExtra("quiz_id",quiz_id);
                            startActivity(intent);
                        })
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                .show();

            }
            else {
                videoPlaying(VIMEO_VIDEO_URL);
            }

            title = chapterContentArrayList.get(0).getChapter_content();

            mMediaController.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        videoPlayingViewModel = new ViewModelProvider(this).get(VideoPlayingViewModel.class);
    }

    public void listeners(){
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });


        videoPlayingViewModel.getCurriculumList(course_id).observe(this, new Observer<List<CurriculumModel>>() {
            @Override
            public void onChanged(List<CurriculumModel> curriculumModels) {
                curriculumAdapter = new CurriculumAdapter(getApplicationContext(),curriculumModels);
                prepareCurrRecyclerView(curriculumModels);
            }
        });



        videoPlayingViewModel.getCommentList(course_id,customer_id).observe(this, new Observer<List<CommentModel>>() {
            @Override
            public void onChanged(List<CommentModel> commentModelList) {
                prepareCommentRecyclerview(commentModelList);
            }
        });



        videoPlaying(VIMEO_VIDEO_URL);

        next_lesson_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i++;
                nextLesson(i);
            }
        });

        send_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_img.setVisibility(View.GONE);
                progressBar_circular.setVisibility(View.VISIBLE);
                postCom();
            }
        });
    }

    private void prepareCurrRecyclerView(List<CurriculumModel> curriculumModelList) {

        //curriculum_recyclerview.setAdapter(curriculumAdapter);
         //curriculumAdapter.notifyDataSetChanged();
    }

    private void prepareCommentRecyclerview(List<CommentModel> commentModelList){
        commentsAdapter = new CommentsAdapter(getApplicationContext(),commentModelList);
        comments_Recyclerview.setAdapter(commentsAdapter);
        commentsAdapter.notifyDataSetChanged();
    }

    public void postCom(){
        String comment = comment_edt.getText().toString().trim();

        videoPlayingViewModel.postComment(course_id,chapt_id,chapt_nme,comment,customer_id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: "+s);
                send_img.setVisibility(View.VISIBLE);
                progressBar_circular.setVisibility(View.GONE);
                comment_edt.setText("");
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


    private void nextLesson(int i2) {
    /*    String uri1 = VimeoClient.getInstance().getCodeGrantAuthorizationURI();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri1));
        startActivity(browserIntent);*/

        try {

            min = "Time - "+chapterContentArrayList.get(i2).getVideo_time()+" mins";
            video_time.setText(min);
            //Toast.makeText(this, ""+chapterContentArrayList.get(i).getChapter_content(), Toast.LENGTH_SHORT).show();
            size = "Lesson " + (i2+1) + " / " + chapterContentArrayList.size();

            chapter_size.setText(size);

            VIMEO_VIDEO_URL = chapterContentArrayList.get(i2).getVideo_url().trim();
            short_desc.setText(chapterContentArrayList.get(i2).getChapter_content());

            assignment = chapterContentArrayList.get(i2).getAssignment_url();
             String download_url="";
            if(!assignment.isEmpty()) {
                String[] sArr = assignment.split("/");
                String fileId = sArr[5];//"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
                download_url = "https://docs.google.com/a/google.com/uc?id=" + fileId;
            }
            quiz_id = chapterContentArrayList.get(i2).getQuiz_id();


            if(VIMEO_VIDEO_URL.equals("") && assignment.length() > 0){
                Toast.makeText(getApplicationContext(), "Its time to do some Assignment: ",Toast.LENGTH_SHORT).show();
               // AltexImageDownloader
                 //       .writeToDisk(getApplicationContext(), assignment, getExternalFilesDir("Robokart/") + "Curriculum/");
                videoPlaying(VIMEO_VIDEO_URL);
                DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(download_url);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                downloadManager.enqueue(request);

            }
            else if (VIMEO_VIDEO_URL.equals("") && quiz_id.length() > 0){
                SharedPreferences quiz = getSharedPreferences("quiz_id",MODE_PRIVATE);
                SharedPreferences.Editor editor = quiz.edit();
                editor.putString("quiz_id",quiz_id);
                editor.apply();
                Log.d(TAG, "Before quiz_id: "+quiz_id);
                Toast.makeText(getApplicationContext(), "Its time for some quiz",Toast.LENGTH_SHORT).show();
                new MaterialDialog.Builder(this)
                        .title("Are you ready to play quiz ?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive((dialog, which) -> {
                            Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                            intent.putExtra("course_id",course_id);
                            intent.putExtra("chpt_id", chapt_id);
                            intent.putExtra("total_number_chapter",total_number_chapter);
                            intent.putExtra("quiz_id",quiz_id);
                            startActivity(intent);
                        })
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();

            }
            else {
                videoPlaying(VIMEO_VIDEO_URL);
            }

            title = chapterContentArrayList.get(i2).getChapter_content();

            mMediaController.setTitle(title);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    String clientId="0993a005480472a69fab10c2f9b8ad0d6bee7acf",
            clientSecret="oVsWjoQ2RHeHvZ8xK3yrtdHrG7YiN+rnHh4qqBfmscDbCwplTFzytAoVIVrXMnAQShuBYuA6fZftYL+AIvX5zRP8JXOs06dQcej1yeL/ACJSGuiKoQJbqdC6CELuP+Pl",
            SCOPE="private public";

    private void videoPlaying(String url){
        //Toast.makeText(this, "url: "+url, Toast.LENGTH_SHORT).show();
  /*     String[] arr=url.split("/");

        Configuration.Builder configBuilder =
                new Configuration.Builder(clientId, clientSecret, SCOPE)
                        .setCacheDirectory(this.getCacheDir());///
        
        VimeoClient.initialize(configBuilder.build());*/
        //Toast.makeText(this, "id: "+arr[4], Toast.LENGTH_SHORT).show();
        //String uri = "https://api.vimeo.com/videos/"+arr[4];// the video uri; if you have a Video, this is video.uri
        //authenticateWithClientCredentials();
/*
        Log.d("vimeo uri: ",""+url);
        VimeoClient.getInstance().fetchNetworkContent("videos/338391481", new ModelCallback<Video>(Video.class) {
            @Override
            public void success(Video video) {
                // use the video
                Log.e("vimeo video","success: "+video);
                mVideoView.setMediaController(mMediaController);
Uri viduri=Uri.parse(video.uri);
                mVideoView.setVideoURI(viduri);
                mVideoView.start();//
            }

            @Override
            public void failure(VimeoError error) {
                // voice the error
                Log.e("vimeo video","failed: "+error);
            }
        });
*/

        VimeoExtractor.getInstance().fetchVideoWithURL(url, null, new OnVimeoExtractionListener() {
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
                            mVideoView.start();

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
                mVideoView.closePlayer();
                Log.d("video fail","Failed to load video: "+throwable.getMessage());
            }
        });


    }

    private void authenticateWithClientCredentials() {
        VimeoClient.getInstance().authorizeWithClientCredentialsGrant(new AuthCallback() {
            @Override
            public void success() {
                String accessToken = VimeoClient.getInstance().getVimeoAccount().getAccessToken();
                Log.d("Success/ Access Token: " , ""+accessToken);
            }

            @Override
            public void failure(VimeoError error) {
                String errorMessage = error.getDeveloperMessage();
                Log.d("Authorization Failure: " , ""+errorMessage);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.seekTo(mSeekPosition);
        }
    }

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
}
