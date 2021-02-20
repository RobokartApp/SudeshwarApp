package com.ark.robokart_robotics.Fragments.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.ark.robokart_robotics.VideoRecord.RecordVideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class StoryFragment extends Fragment {

GridView gridView;
    ProgressBar progressBar;
    ArrayList<DoubtItem> storyItems;
    String TAG="Story Frag";
    String user_id;
    private RequestQueue requestQueue;
    LinearLayout create_ll;
    TextView createBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        listener();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userdetails",MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","848");
        storyItems=new ArrayList<>();
        getStories();
    }
    private void init(View view) {
        gridView=view.findViewById(R.id.gridView);
        progressBar=view.findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(getContext());
        create_ll=view.findViewById(R.id.createBtn);
        create_ll.setVisibility(View.GONE);
        createBtn=view.findViewById(R.id.tv_NewDoubt);
    }
    private void listener(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecordVideoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getStories(){
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.fetch_story_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("ask_doubt");
                ArrayList<DoubtItem> temp_list=new ArrayList<>();
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo ask",response);
                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            DoubtItem doubt = new DoubtItem(
                                    json.getString("post_id"),
                                    json.getString("post_vid"),//0
                                    json.getString("post_desc")
                            );
                            temp_list.add(doubt);
                        }

                        storyItems.addAll(temp_list);
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
                Log.d(TAG, "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Volley error: "+error.getMessage());
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
                progressBar.setVisibility(View.GONE);
                storyAdapter adapter=new storyAdapter(getActivity(),storyItems);
                gridView.setAdapter(adapter);
                if (storyItems.size()<1)
                {
                    gridView.setVisibility(View.GONE);
                    create_ll.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}