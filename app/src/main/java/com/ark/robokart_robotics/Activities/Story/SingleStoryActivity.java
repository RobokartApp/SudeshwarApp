package com.ark.robokart_robotics.Activities.Story;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.AskDoubt.DoubtAllComment;
import com.ark.robokart_robotics.Activities.AskDoubt.SinglePostActivity;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Fragments.Stories.StoriesFragment;
import com.ark.robokart_robotics.Fragments.Stories.VideoItem;
import com.ark.robokart_robotics.R;
import com.ark.robokart_robotics.VideoRecord.RecordVideoActivity;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class SingleStoryActivity extends AppCompatActivity implements Player.EventListener{

    private RequestQueue requestQueue;
    String user_id,postId,profileImg,videoURL,videoTitle,noLike,noComment,noView,profileName,isLike,post_id,by_user="";
    ImageView play_pause,back_btn,error_img;
    CircleImageView civ_profile_img;
    LinearLayout create_btn;
    ProgressBar p_bar;
    Context context;

    ImageView view_btn,like_btn,share_btn,comment_btn,more_options;

    LinearLayout user_info,lcs_right;
    TextView vidTitle;
    TextView like_count,view_count,profile_Name,comment_count;
    CircleImageView profile_Img;

    boolean is_user_stop_video=false;
    boolean from=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story);
        context=SingleStoryActivity.this;

        init();

        civ_profile_img=findViewById(R.id.civ_user_profile);
        play_pause=findViewById(R.id.play_pause);
        p_bar=findViewById(R.id.p_bar);
        create_btn=findViewById(R.id.lnr_Create);
        back_btn=findViewById(R.id.back_btn);
        requestQueue = Volley.newRequestQueue(context);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","");
        String url = sharedPreferences.getString("customer_image","https://img.icons8.com/officel/2x/user.png");

        Glide.with(context).load(url).disallowHardwareConfig().into(civ_profile_img);

        Bundle bundle=getIntent().getExtras();

        if(getIntent().hasExtra("post_id")){
            post_id=bundle.getString("post_id");
            Log.i("SingleStoryAct","post_id:"+post_id);
        }else {
            post_id = "1";
            Log.i("SingleStoryAct","no bundle no extras");
        }

        from= getIntent().hasExtra("from");

        Call_Api_For_get_SingleVideos();
    }

    private void init() {

        view_btn=findViewById(R.id.view_btn_iv);
        view_btn.setEnabled(false);
        user_info=findViewById(R.id.user_info);
        lcs_right=findViewById(R.id.lnr_Right);

        error_img=findViewById(R.id.error_img);
        error_img.setVisibility(GONE);

        more_options=findViewById(R.id.options_btn_iv);
        more_options.setVisibility(View.GONE);

        like_btn=findViewById(R.id.like_btn_iv);
        comment_btn=findViewById(R.id.comment_btn_iv);
        share_btn=findViewById(R.id.share_btn_iv);

        like_count=findViewById(R.id.no_likes);
        comment_count=findViewById(R.id.no_comments);
        view_count=findViewById(R.id.no_videws);
        profile_Name=findViewById(R.id.profile_name_story);
        profile_Img=findViewById(R.id.profile_img_story);

        vidTitle=findViewById(R.id.textVideoTitle);
    }

    private void Call_Api_For_get_SingleVideos() {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.single_story_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                ArrayList<VideoItem> temp_list=new ArrayList<>();
                JSONArray courses = jsonObject.getJSONArray("story");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo story",response);
                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            postId=json.getString("post_id");
                            videoURL=json.getString("post_vid");
                            videoTitle=json.getString("post_desc");
                            noLike=json.getString("post_likes");
                            noComment=json.getString("post_comments");
                            noView=json.getString("post_views");
                            profileImg=json.getString("post_profile_img");
                            profileName=json.getString("post_profile_name");
                            isLike=json.getString("isLiked");
                            by_user=json.getString("by_user");

                        }
                    }
                    catch (Exception e){
                        Log.d("volley respo exception", e.getMessage());
                    }

                }else if (status == 0) {
                    error_img.setVisibility(View.VISIBLE);
                    p_bar.setVisibility(GONE);
                    Log.i("status 0",error_msg);
                    postId="NA";
                    videoURL="NA";
                    videoTitle="This post has been deleted!";
                    noLike="NA";
                    noComment="NA";
                    noView="NA";
                    profileImg="NA";
                    profileName="NA";
                    isLike="NA";
                    by_user="NA";
                    Toast.makeText(getApplicationContext(), "No data found!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Stories Frag", "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Stories Frag", "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("customer_id",user_id);
                parameters.put("post_id",post_id);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                p_bar.setVisibility(GONE);
                listener();
                Set_Player();
            }
        });

    }
    private void listener(){
        vidTitle.setText(videoTitle);

        like_count.setText(noLike);
        comment_count.setText(noComment);
        view_count.setText(noView);

        if (by_user.equals(user_id)){
            more_options.setVisibility(View.VISIBLE);
        }

        error_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(postId);
                //listener.onItemClick(postion,item,v);
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_permissions(SingleStoryActivity.this)) {
                    Intent intent = new Intent(context, RecordVideoActivity.class);
                    startActivity(intent);
                }
            }
        });
        civ_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(isLike.equals("yes")){
            like_btn.setImageResource(R.drawable.heart);
            like_btn.setImageTintList(ContextCompat.getColorStateList(context, R.color.orange));
            like_btn.setEnabled(false);
        }

        String imgUrl="https://robokart.com/admin/assets/uploads/images/customer/"+profileImg;
        Glide.with(context).load(imgUrl).disallowHardwareConfig().into(profile_Img);
        profile_Name.setText(profileName);

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "view clicked"+dataList.get(postion).isLike, Toast.LENGTH_SHORT).show();
            }
        });

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "liked "+dataList.get(postion).postId, Toast.LENGTH_SHORT).show();
                like_btn.setImageResource(R.drawable.heart);
                like_btn.setImageTintList(ContextCompat.getColorStateList(context, R.color.orange));
                if(!like_count.getText().toString().equals(""))
                {
                    int i=Integer.parseInt(like_count.getText().toString());
                    i++;
                    like_count.setText(""+i);
                }else
                    like_count.setText("1");

                like_btn.setEnabled(false);
                sendLcs("like",postId);
            }
        });
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DoubtAllComment.class);
                intent.putExtra("post_id",postId);
                intent.putExtra("story","ok");
                if (intent.resolveActivity(getPackageManager()).getPackageName().equals("com.ark.robokart_robotics"))
                    startActivityForResult(intent,121);
               // listener.onItemClick(postion,item,view);
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage= "\nFound this amazing story on Robokart app. Download the app for free and take a look at this:\n";
                    shareMessage = shareMessage + "https://robokart.com/app/Story?id="+post_id;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    context.startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));

                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        vidTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lcs_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        sendLcs("view",postId);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from)
                    onBackPressed();
                else {
                    finish();
                    Intent intent = new Intent(SingleStoryActivity.this, HomeActivity.class);
                    intent.putExtra("post", "story");
                    startActivity(intent);
                }
            }
        });

    }

    private void showDeleteDialog(String postId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        //ViewGroup viewGroup = mContext.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null, false);

        Button delete_post=dialogView.findViewById(R.id.button_delete);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                deletePost(postId);
                                alertDialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                //finish();
                            }
                        })
                        .show();
                //deletePost(postId);
                //alertDialog.dismiss();

            }
        });

    }

    private void deletePost(String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "delete_story_api.php", response -> {
            Log.d("delete story respo ",response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("success_code");
                if (status == 1) {
                    onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("respo ask",response);

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
                parameters.put("post_id",postId);
                return parameters;
            }
        };
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));;
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
                Toast.makeText(SingleStoryActivity.this, "Your post has been deleted!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    boolean is_visible_to_user;

    // when we swipe for another video this will relaese the privious player
    SimpleExoPlayer privious_player;


    @Override
    public void onPause() {
        super.onPause();
        play_pause.setVisibility(View.VISIBLE);
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        play_pause.setVisibility(View.VISIBLE);
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(privious_player!=null){
            privious_player.release();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        is_visible_to_user=true;
    }

    // Bottom all the function and the Call back listener of the Expo player
    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }


    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }


    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if(playbackState==Player.STATE_BUFFERING){
            p_bar.setVisibility(View.VISIBLE);
        }
        else if(playbackState==Player.STATE_READY){
            p_bar.setVisibility(GONE);
        }


    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


    @Override
    public void onSeekProcessed() {

    }

    public void Set_Player(){
        //Toast.makeText(context, ""+is_visible_to_user, Toast.LENGTH_SHORT).show();


        //HttpProxyCacheServer proxy = Rbk.getProxy(context);
        String proxyUrl = videoURL;
        LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, 16),1*1024, 1*1024, 500, 1024, -1, true);


        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));


        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(proxyUrl));

        Log.d("Story frag url: ",videoURL);
        Log.d("Story frag proxy url: ",proxyUrl);


        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


      //  View layout=layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView=findViewById(R.id.playerview);
        playerView.setPlayer(player);


        player.setPlayWhenReady(is_visible_to_user);
        privious_player=player;
        play_pause.setVisibility(View.GONE);
//        swipe_count++;

        //   Call_Api_For_Singlevideos(currentPage);
        final ConstraintLayout mainlayout = findViewById(R.id.main_layout);
        playerView.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if(!player.getPlayWhenReady()){
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }else{
                        is_user_stop_video=true;
                        privious_player.setPlayWhenReady(false);
                    }
                    if (play_pause.getVisibility()==View.GONE)
                        play_pause.setVisibility(View.VISIBLE);
                    else
                        play_pause.setVisibility(View.GONE);

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    //Show_video_option(item);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if(!player.getPlayWhenReady()){
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }

                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public boolean check_permissions(Activity context) {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(PERMISSIONS, 2);
            }
        }else {

            return true;
        }

        return false;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // TODO Extract the data returned from the child Activity.
            String returnValue = data.getStringExtra("comment");
            //Toast.makeText(context, ""+returnValue, Toast.LENGTH_SHORT).show();

            if (returnValue.equals("minus"))
                noComment=""+(Integer.parseInt(noComment)-1);
            else
                noComment=""+(Integer.parseInt(noComment)+1);

            comment_count.setText(noComment);

        }


    }

    void sendLcs(String lcs,String postId) {
        Log.i("sending lcs","id:"+postId);
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.send_video_lcs, response -> {
            Log.d("like respo ",response);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Vid Adapter", "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("post_id",postId);
                parameters.put("user_id", user_id);
                parameters.put(lcs,"1");
                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (from)
            super.onBackPressed();
        else {
            finish();
            Intent intent = new Intent(SingleStoryActivity.this, HomeActivity.class);
            intent.putExtra("post", "story");
            startActivity(intent);
        }
    }
}