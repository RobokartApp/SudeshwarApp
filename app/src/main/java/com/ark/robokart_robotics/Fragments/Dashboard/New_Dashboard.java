package com.ark.robokart_robotics.Fragments.Dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public New_Dashboard(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Menu menu= HomeActivity.bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        return inflater.inflate(R.layout.fragment_dashboard_new,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueueST = Volley.newRequestQueue(getContext());
        cardMaker = view.findViewById(R.id.cardMaker);
        cardAtl = view.findViewById(R.id.cardAtl);
        cardFreeCourse=view.findViewById(R.id.cardFreeCourse);
        play_quiz_challenge = view.findViewById(R.id.play_quiz_challenge);
        recyclerView=view.findViewById(R.id.rvPopularVideos);
        share_app=view.findViewById(R.id.share_app);

        progressBar=view.findViewById(R.id.progressBar);
        story_recyclerv=view.findViewById(R.id.stories_recycler);

        //VidId=MainActivity.vidId;//{"2MDVqbA-170","ALTLequqJQk", "na3_0VmTiAw"};
//int size=MainActivity.vidId.size();
        //      Toast.makeText(getContext(), "size: "+size, Toast.LENGTH_SHORT).show();

        //Activity activity=getActivity();

        getIds();

        getStories();

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
