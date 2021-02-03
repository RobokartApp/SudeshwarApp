package com.ark.robokart_robotics.Activities.FreeCourses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Adapters.PopularRecycler;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addCategory {
    TextView cat_head;
    Context context;

    String title;
    PopularRecycler adapter;
    RecyclerView recyclerView;
    LinearLayout forCategory;
    private final RequestQueue requestQueue;

    public addCategory(Context context,LinearLayout forCategory, String title_cat){
        this.title=title_cat;
        this.context=context;
        this.forCategory=forCategory;


        requestQueue = Volley.newRequestQueue(context);


        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout category= (ConstraintLayout) vi.inflate(R.layout.youtube_category, null);


        cat_head=category.findViewById(R.id.cat_head);
        recyclerView=category.findViewById(R.id.recycler_youtube);

        cat_head.setText(title);
        //adapter=new PopularRecycler(context,vid_ids,vid_titles);
        //recyclerView.setAdapter(adapter);
        getIds(title_cat);

        forCategory.addView(category);
    }
    public void getIds(String category){
        ArrayList<String> ids=new ArrayList<>();
        ArrayList<String> title=new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.youtube_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("youtube");

                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                //Toast.makeText(context, ""+categories, Toast.LENGTH_LONG).show();
                ids.clear();
                title.clear();
                if (status == 1) {
                    try{


                        for(int i = 0; i< atl.length();i++){

                            JSONObject json = atl.getJSONObject(i);
                            ids.add(i,""+json.getString("id"));
                            title.add(i,""+json.getString("title"));

                        }

                    }
                    catch (Exception e){
                        Log.e("json getting data",e.getMessage());
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
                parameters.put("category",category);
                return parameters;
            }
        }
                ;
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                adapter=new PopularRecycler(context,ids,title);
                 recyclerView.setAdapter(adapter);

            }
        });
    }
}
