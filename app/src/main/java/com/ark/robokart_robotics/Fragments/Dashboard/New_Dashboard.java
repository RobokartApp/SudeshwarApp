package com.ark.robokart_robotics.Fragments.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.AtlChooseStandard.AtlChooseStandard;
import com.ark.robokart_robotics.Activities.FreeCourses.YouTube;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.Profile.DetailAdapter;
import com.ark.robokart_robotics.Activities.Profile.LevelAdapter;
import com.ark.robokart_robotics.Activities.Profile.NewProfileAct;
import com.ark.robokart_robotics.Activities.Quiz.DailyQuizActivity;
import com.ark.robokart_robotics.Activities.Refer.ReferActivity;
import com.ark.robokart_robotics.Adapters.HomeStoryAdapter;
import com.ark.robokart_robotics.Adapters.PopularRecycler;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class New_Dashboard extends Fragment {

    public TextView play_quiz_challenge;

    public CardView cardMaker, cardAtl,cardFreeCourse;
    RecyclerView recyclerView,story_recyclerv;
    PopularRecycler adapter;
    HomeStoryAdapter adapterST;
    TextView share_app;
    ArrayList<String> vidId=new ArrayList<String>();
    ArrayList<String> title=new ArrayList<String>();
    public static ArrayList<String> instructions=new ArrayList<>();
    private RequestQueue requestQueue,requestQueueST;
    ProgressBar progressBar;
    RecyclerView recyclerView_level;
    RecyclerView level_details_list;
    int level;
    SharedPreferences sharedpreferences;

    public New_Dashboard(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Menu menu= HomeActivity.bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        return inflater.inflate(R.layout.fragment_dashboard_new,container,false);
    }
    DetailAdapter detailAdapter;
    String[] level1,level2,level3,level4,level5,level6,level7,level8,level9,level10;
    String[][] progress=new String[11][11];
    TextView level_head;
    double percent=0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueueST = Volley.newRequestQueue(getContext());
        cardMaker = view.findViewById(R.id.cardMaker);
        cardAtl = view.findViewById(R.id.cardAtl);
        cardFreeCourse=view.findViewById(R.id.cardFreeCourse);
        play_quiz_challenge = view.findViewById(R.id.play_quiz_challenge);
        recyclerView=view.findViewById(R.id.rvPopularVideos);
        share_app=view.findViewById(R.id.share_app);

        progressBar=view.findViewById(R.id.progressBar);
        story_recyclerv=view.findViewById(R.id.stories_recycler);

        recyclerView_level=view.findViewById(R.id.recycler_level);

        level_head=view.findViewById(R.id.level_head_tv);
        progress[0]=new String[]{"100% completed"};
        //progress[1]=new String[]{"100% completed","100% completed","0% completed"};

        sharedpreferences= getActivity().getSharedPreferences("level_details", Context.MODE_PRIVATE);

        level_details_list=view.findViewById(R.id.list_level_detail);
        level1=new String[]{"Register with Robokart."};
        level2=new String[]{"Refer first friend.","Refer second friend.","Refer third friend."};
        level3=new String[]{"Day 1 attendance.","Day 2 attendance.","Day 3 attendance.","Day 4 attendance.","Day 5 attendance."};
        level4=new String[]{"Watch 10 stories.","Answer 3 doubts."};
        level5=new String[]{"Buy one course."};
        level6=new String[]{"Referred friend buy a course"};
        level7=new String[]{"Post first doubt","Post second doubt","Post third doubt"};
        level8=new String[]{"Post first story","Post second story","Post third story","Post fourth story","Post fifth story"};
        level9=new String[]{"Overall 10 hours of play(Free course videos)."};
        level10=new String[]{"Attempt daily quiz for the complete week."};

        String[] listItem = {"level1","Refer one friend.","Daily visit Robokart app for 5 days.","Watch 10 stories and answer 3 doubts.",
                "Buy one course.","Referred friends buy one course.",};

        //VidId=MainActivity.vidId;//{"2MDVqbA-170","ALTLequqJQk", "na3_0VmTiAw"};
//int size=MainActivity.vidId.size();
        //      Toast.makeText(getContext(), "size: "+size, Toast.LENGTH_SHORT).show();

        //Activity activity=getActivity();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        try {
            if (getDateDiff()>1) {
                editor.putInt("day_attend", 1);
                Date date = new Date();
                SimpleDateFormat ft =
                        new SimpleDateFormat("dd-MM-yyyy");
                String today=ft.format(date);
                editor.putString("last_date",today);
                editor.apply();
            }else if (getDateDiff()==1){
                Date date = new Date();
                SimpleDateFormat ft =
                        new SimpleDateFormat("dd-MM-yyyy");
                String today=ft.format(date);
                editor.putString("last_date",today);
                editor.putInt("day_attend",(sharedpreferences.getInt("day_attend",1)+1));
                editor.apply();
            }

        } catch (ParseException e) {
            Log.e("New dash date",e.getMessage());
        }

        getIds();

        getStories();
        getProgress();

        //getInstructions();
        cardFreeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(), YouTube.class);
                startActivity(intent);

            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ReferActivity.class));
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

        cardMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment uploadDoc= new DashboardFragment();
                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim)
                        .replace(R.id.mainFrameLayout, uploadDoc, "mycourses")
                        .addToBackStack(null)
                        .commit();


            }
        });

        cardAtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AtlChooseStandard.class);
                startActivity(intent);
            }
        });

        play_quiz_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DailyQuizActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getLevel() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST +"fetch_user_level", response -> {
            try {
                Log.d("fetch user level",response);
                JSONObject jsonObject = new JSONObject(response);

                int status = jsonObject.getInt("success_code");

                if (status == 1) {
                    switch (level){
                        case 1:detailAdapter= new DetailAdapter(getContext(),level1,progress[0],percent);break;
                        case 2:detailAdapter= new DetailAdapter(getContext(),level2,progress[1],percent);break;
                        case 3:detailAdapter= new DetailAdapter(getContext(),level3,progress[2],percent);break;
                        case 4:detailAdapter= new DetailAdapter(getContext(),level4,progress[3],percent);break;
                        case 5:detailAdapter= new DetailAdapter(getContext(),level5,progress[4],percent);break;
                        case 6:detailAdapter= new DetailAdapter(getContext(),level6,progress[5],percent);break;
                        case 7:detailAdapter= new DetailAdapter(getContext(),level7,progress[6],percent);break;
                        case 8:detailAdapter= new DetailAdapter(getContext(),level8,progress[7],percent);break;
                        case 9:detailAdapter= new DetailAdapter(getContext(),level9,progress[8],percent);break;
                        case 10:detailAdapter= new DetailAdapter(getContext(),level10,progress[9],percent);break;
                    }
                    level_details_list.setAdapter(detailAdapter);
                    LevelAdapter levelAdapter=new LevelAdapter(getContext(), level, new LevelAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int positon, LevelAdapter.VideoInfoHolder holder, View view) {
                            //Toast.makeText(getContext(), ""+holder.status_text.getText(), Toast.LENGTH_SHORT).show();
                            //holder.down_arrow.setVisibility(View.VISIBLE);
                            switch (positon){
                                case 0:
                                    progress[0]=new String[]{"100% completed"};
                                    detailAdapter=new DetailAdapter(getActivity(),level1,progress[positon],percent);
                                    level_head.setText("Level 1 :");
                                    break;
                                case 1:
                                    //progress[1]=new String[]{"100% completed","100% completed","0% completed"};
                                    detailAdapter=new DetailAdapter(getActivity(),level2,progress[positon],percent);
                                    level_head.setText("Level 2 :");
                                    break;
                                case 2:
                                    //progress[2]=new String[]{"0% completed","0% completed","0% completed","0% completed","0% completed"};
                                    detailAdapter=new DetailAdapter(getActivity(),level3,progress[positon],percent);
                                    level_head.setText("Level 3 :");
                                    break;
                                case 3:
                                    //progress[3]=new String[]{"0% completed","0% completed","0% completed","0% completed","0% completed"};
                                    detailAdapter=new DetailAdapter(getActivity(),level4,progress[positon],percent);
                                    level_head.setText("Level 4 :");
                                    break;
                                case 4:
                                    detailAdapter=new DetailAdapter(getActivity(),level5,progress[positon],percent);
                                    level_head.setText("Level 5 :");
                                    break;
                                case 5:
                                    detailAdapter=new DetailAdapter(getActivity(),level6,progress[positon],percent);
                                    level_head.setText("Level 6 :");
                                    break;
                                case 6:
                                    detailAdapter=new DetailAdapter(getActivity(),level7,progress[positon],percent);
                                    level_head.setText("Level 7 :");
                                    break;
                                case 7:
                                    detailAdapter=new DetailAdapter(getActivity(),level8,progress[positon],percent);
                                    level_head.setText("Level 8 :");
                                    break;
                                case 8:

                                    String time="";
                                    int sec=sharedpreferences.getInt("time",1);
                                    int second = sec % 60;
                                    int hour = sec / 60;
                                    int minute = hour % 60;
                                    hour =hour / 60;
                                    double perc=(double)sec*100/36000;
                                    Log.e("New Dash progress",""+percent);
                                    progress[8]=new String[]{hour+"Hrs, "+minute+"Mins completed"};
                                    detailAdapter=new DetailAdapter(getActivity(),level9,progress[positon],perc);
                                    level_head.setText("Level 9 :");
                                    break;
                                case 9:
                                    detailAdapter=new DetailAdapter(getActivity(),level10,progress[positon],percent);
                                    level_head.setText("Level 10 :");
                                    break;


                            }

                            level_details_list.setAdapter(detailAdapter);
                            detailAdapter.notifyDataSetChanged();

                        }
                    });
                    recyclerView_level.setAdapter(levelAdapter);
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
                parameters.put("cust_id",HomeActivity.user_id);
                return parameters;
            }
        };

        requestQueue.add(request);
    }
    private long getDateDiff() throws ParseException {
        Date date = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat("dd-MM-yyyy");
        String today=ft.format(date);
        String last_date=sharedpreferences.getString("last_date","23-03-2021");
        Date date1=ft.parse(last_date);
        date=ft.parse(today);
        long difference_In_Time=date.getTime()-date1.getTime();
        long days=(difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;
        Log.e("date diff",""+(days));
        return days;
    }
    private void getProgress(){
        StringRequest request2 = new StringRequest(Request.Method.POST, ApiConstants.HOST +"get_level_detail", response -> {
            try {
                Log.d("get level detail",response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray level_detail = jsonObject.getJSONArray("user_level");
                String level_string = jsonObject.getString("current_level");
                level=Integer.parseInt(level_string)+1;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                Log.e("user_level editor",""+level);
                editor.putString("user_level",""+level);
                editor.apply();

                level_head.setText("Level "+level+" :");
                int status = jsonObject.getInt("success_code");

                if (status == 1) {
                    for (int i=0;i<level_detail.length();i++){
                        JSONObject object=level_detail.getJSONObject(i);
                        int l=object.getInt("level");
                        int c=object.getInt("contest");

                        progress[l][c]=object.getString("progress");
                    }
                    int sec=sharedpreferences.getInt("time",1);
                    if (sec>=36000 && level==9)level=10;

                    int days=sharedpreferences.getInt("day_attend",1);
                    Log.e("days attend",""+days);
                    for(int i=0;i<days;i++){
                        progress[2][i]="100% Completed";
                    }


                    getLevel();
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
                parameters.put("cust_id",HomeActivity.user_id);
                return parameters;
            }
        };
        requestQueue.add(request2);
    }
    private void getStories() {
        ArrayList<String> storyList=new ArrayList<String>();
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.home_story, response -> {
            try {
                Log.d("dashboard stories",response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("stories");
                int status = jsonObject.getInt("success_code");
                storyList.clear();

                if (status == 1) {
                    try{
                        for(int i = 0; i< atl.length();i++){
                            JSONObject json = atl.getJSONObject(i);

                            storyList.add(json.getString("id"));

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
                parameters.put("category","learn in 60");
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                adapterST=new HomeStoryAdapter(getContext(),storyList);
                story_recyclerv.setAdapter(adapterST);

            }
        });
    }

    public void getIds(){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.youtube_api, response -> {
            try {
                Log.d("dashboard ids",response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("youtube");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                //Toast.makeText(getContext(), ""+response, Toast.LENGTH_LONG).show();
                vidId.clear();

                if (status == 1) {
                    try{
                        for(int i = 0; i< atl.length();i++){
                            JSONObject json = atl.getJSONObject(i);
                            vidId.add(i,""+json.getString("id"));
                            title.add(i,""+json.getString("title"));

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
                parameters.put("category","learn in 60");
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                adapter=new PopularRecycler(getContext(),vidId,title);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getInstructions(){
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.fetch_liked_user, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("liked_user");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.i("Liked Users",response);
                //Toast.makeText(getContext(), ""+response, Toast.LENGTH_LONG).show();


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
                parameters.put("post_id","69");
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
            }
        });
    }

}
