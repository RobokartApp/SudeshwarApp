package com.ark.robokart_robotics.Fragments.Stories;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.AskDoubt.DoubtAllComment;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.ark.robokart_robotics.VideoRecord.RecordVideoActivity;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.Allocator;
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
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */

// this is the main view which is show all  the video in list
public class StoriesFragment extends Fragment implements Player.EventListener{

    View view;
    Context context;

    RecyclerView recyclerView;
    ArrayList<VideoItem> data_list;
    int currentPage=-1;
    LinearLayoutManager layoutManager;
    ProgressBar p_bar;
    SwipeRefreshLayout swiperefresh;
    boolean is_user_stop_video=false;
    private RequestQueue requestQueue;
    public static String user_id;

    String type="related";

    public StoriesFragment() {
        // Required empty public constructor
    }

    int swipe_count=0,i_page=0;
    ImageView play_pause;
    CircleImageView civ_profile_img;
    LinearLayout create_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        context=getContext();

        String strtext="";
        if (getArguments()!=null) {
            strtext = getArguments().getString("id");
            i_page= Integer.parseInt(strtext);
        }
        else
            strtext="NA";

        Log.i("story Data id",strtext);


        Menu menu= HomeActivity.bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(3);
        menuItem.setChecked(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        civ_profile_img=view.findViewById(R.id.civ_user_profile);
        play_pause=view.findViewById(R.id.play_pause);
        p_bar=view.findViewById(R.id.p_bar);
        create_btn=view.findViewById(R.id.lnr_Create);

        requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userdetails",MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","");
        String url = sharedPreferences.getString("customer_image","https://img.icons8.com/officel/2x/user.png");

        Glide.with(getContext()).load(url).disallowHardwareConfig().into(civ_profile_img);

        recyclerView=view.findViewById(R.id.recylerview);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper =  new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        listener();


        swiperefresh=view.findViewById(R.id.swiperefresh);
        swiperefresh.setProgressViewOffset(false, 0, 200);

        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call_Api_For_get_Allvideos();
            }
        });

        Call_Api_For_get_Allvideos();


        return view;
    }

    private void listener() {
        // this is the scroll listener of recycler view which will tell the current item number
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no=scrollOffset / height;

                if(page_no!=currentPage ){
                    currentPage=page_no;

                    Release_Privious_Player();
                    Set_Player(currentPage);

                }
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_permissions(getActivity())) {
                    Intent intent = new Intent(getContext(), RecordVideoActivity.class);
                    startActivity(intent);
                }
            }
        });
        civ_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    Home_Adapter adapter;
    public void Set_Adapter(){

        adapter=new Home_Adapter(context, data_list, new Home_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, final VideoItem item, View view) {
                switch(view.getId()) {



                    case R.id.comment_btn_iv:
                        Intent intent=new Intent(context, DoubtAllComment.class);
                        intent.putExtra("post_id",item.postId);
                        intent.putExtra("story","ok");
                        Log.e("story Frag",""+intent.resolveActivity(context.getPackageManager()).getPackageName() );
                        if (intent.resolveActivity(context.getPackageManager()).getPackageName().equals("com.ark.robokart_robotics"))
                            startActivityForResult(intent,121);
                        break;
                    case R.id.options_btn_iv:
                        showDeleteDialog(item.postId,postion);
                        break;


                }

            }
        });

        adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);
        Log.i("StoryFrag","scroll:"+i_page);
        if (getArguments()!=null)
            recyclerView.scrollToPosition(i_page);

    }

    private void showDeleteDialog(String postId,int position) {
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
                                deletePost(postId,position);
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

    private void deletePost(String postId,int position) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "delete_story_api.php", response -> {
            Log.d("delete story respo ",response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("success_code");
                if (status == 1) {
                    //recyclerView.removeViewAt(position);
                    privious_player.release();
                    data_list.remove(position);
                    adapter.notifyDataSetChanged();
                    Set_Player(position);
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
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));;
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
                Toast.makeText(context, "Your post has been deleted!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra("comment");
                    //Toast.makeText(context, ""+returnValue, Toast.LENGTH_SHORT).show();
                    VideoItem item=data_list.get(currentPage);
                    if (returnValue.equals("minus"))
                        item.noComment=""+(Integer.parseInt(item.noComment)-1);
                    else
                        item.noComment=""+(Integer.parseInt(item.noComment)+1);
                    data_list.remove(currentPage);
                    data_list.add(currentPage,item);
                    adapter.notifyDataSetChanged();
                }


    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Allvideos() {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.get_stories_api, response -> {
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
                            VideoItem vi=new VideoItem();
                            vi.postId=json.getString("post_id");
                            vi.videoURL=json.getString("post_vid");
                            vi.videoTitle=json.getString("post_desc");
                            vi.noLike=json.getString("post_likes");
                            vi.noComment=json.getString("post_comments");
                            vi.noView=json.getString("post_views");
                            vi.profileImg=json.getString("post_profile_img");
                            vi.profileName=json.getString("post_profile_name");
                            vi.isLike=json.getString("isLiked");
                            vi.by_user=json.getString("by_user");

                            temp_list.add(vi);

                            if(!temp_list.isEmpty()) {
                                currentPage=-1;
                                data_list=new ArrayList<>();
                                data_list.addAll(temp_list);
                                Set_Adapter();
                            }
                        }

                    }
                    catch (Exception e){
                        Log.d("volley respo exception", e.getMessage());
                    }

                }else if (status == 0) {
                    Log.i("status 0",error_msg);
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
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                p_bar.setVisibility(GONE);
                swiperefresh.setRefreshing(false);
                //Set_Player(currentPage);
                Log.i("Api End","page:"+currentPage);
            }
        });

    }


    // this will call when swipe for another video and
    // this function will set the player to the current video
    public void Set_Player(final int currentPage){
        //Toast.makeText(context, ""+is_visible_to_user, Toast.LENGTH_SHORT).show();

        final VideoItem item= data_list.get(currentPage);

        Call_cache();


        //HttpProxyCacheServer proxy = Rbk.getProxy(context);
        String proxyUrl = item.videoURL;
//        LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, 16),500, 1000, 500, 800, -1, true);

/*
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
     //   final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));


        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(proxyUrl));
*/
        Log.d("Story frag url: ",item.videoURL);
        Log.d("Story frag proxy url: ",proxyUrl);


       // player.prepare(videoSource);
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
        // playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(proxyUrl));
        player.setMediaItem(mediaItem);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);



        View layout=layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView=layout.findViewById(R.id.playerview);
        playerView.setPlayer(player);

        player.setPlayWhenReady(true);
        player.prepare();

        player.setPlayWhenReady(is_visible_to_user);
        privious_player=player;
        play_pause.setVisibility(View.GONE);
        swipe_count++;


        //Toast.makeText(context, "duration:"+player.getContentDuration(), Toast.LENGTH_SHORT).show();

     //   Call_Api_For_Singlevideos(currentPage);
      //  final ConstraintLayout mainlayout = layout.findViewById(R.id.main_layout);
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

    SimpleExoPlayer cache_player;

    public void Call_cache(){
        if(currentPage+1<data_list.size()){

            if(cache_player!=null)
                cache_player.release();


            String proxyUrl = (data_list.get(currentPage+1).videoURL);

            cache_player = new SimpleExoPlayer.Builder(context).build();
//            LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, 16),500, 1000, 500, 1000, -1, true);
     /*       DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            cache_player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));
            cache_player.prepare(videoSource);
            */
            // playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(proxyUrl));
            cache_player.setMediaItem(mediaItem);

        }

    }

    // this will call when go to the home tab From other tab.
    // this is very importent when for video play and pause when the focus is changes
    boolean is_visible_to_user;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user=isVisibleToUser;

        if(privious_player!=null && (isVisibleToUser && !is_user_stop_video)){
            privious_player.setPlayWhenReady(true);
        }
        else if(privious_player!=null && !isVisibleToUser){
            privious_player.setPlayWhenReady(false);
        }

    }



    // when we swipe for another video this will relaese the privious player
    SimpleExoPlayer privious_player;
    public void Release_Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }

    public boolean is_fragment_exits(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        return fm.getBackStackEntryCount() != 0;

    }



    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    public void onResume() {
        super.onResume();
        if((privious_player!=null && (is_visible_to_user && !is_user_stop_video)) && !is_fragment_exits() ){
            privious_player.setPlayWhenReady(true);
        }
    }



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

    int iteration=1;
    // Bottom all the function and the Call back listener of the Expo player
    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        if ((iteration%2)==0) {
            int curIndex = cache_player.getCurrentWindowIndex();
            long curDur = timeline.getWindow(curIndex, new Timeline.Window()).getDurationMs();
            long trackSec=TimeUnit.MILLISECONDS.toSeconds(curDur);

            double perc80=(int)trackSec*0.8;

            int time80=(int)perc80;//this is 80% time in int format
            //Toast.makeText(context, ""+(int)perc80, Toast.LENGTH_SHORT).show();

            setTime(time80);
            iteration++;
        }else
            iteration++;
    }

    private void setTime(int time80) {
        new CountDownTimer(time80*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // mTextField.setText("done!");
               // Toast.makeText(context, "id:"+data_list.get(currentPage).postId, Toast.LENGTH_SHORT).show();
                try {
                    sendViewComplete(data_list.get(currentPage).postId);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void sendViewComplete(String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.send_complete_view, response -> {
            Log.d("send completeView",response);
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
                parameters.put("user_id", StoriesFragment.user_id);

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));;
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
            }
        });
    }


    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        //Toast.makeText(context, "track changed"+trackGroups.length+" #selection:"+trackSelections.length, Toast.LENGTH_SHORT).show();
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
if (playbackState==Player.STATE_ENDED){

   // Toast.makeText(context, "state ended!", Toast.LENGTH_SHORT).show();
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


}
