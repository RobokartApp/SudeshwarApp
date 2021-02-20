package com.ark.robokart_robotics.Fragments.AskDoubt;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.MyPostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.ark.robokart_robotics.Fragments.AskDoubt.AskDoubtFragment.progressBar;

public class PostRepository {
    private static final String TAG = "PostRepository";

    private final Application application;

    private final RequestQueue requestQueue;

    private final MutableLiveData<List<MyPostModel>> myPostLiveDataList = new MutableLiveData<>();

    private final ArrayList<MyPostModel> myPostListModelArrayList = new ArrayList<>();
    String user_id;

    public PostRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);

        SharedPreferences sharedPreferences = application.getSharedPreferences("userdetails",MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","848");

    }


    public MutableLiveData<List<MyPostModel>> getMyPostsList(){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.ask_doubt_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("ask_doubt");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
Log.d("respo ask",response);
                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            MyPostModel course = new MyPostModel(
                                    json.getString("post_id"),//0
                                    json.getString("post_img"),//1
                                    json.getString("post_title"),//2
                                    json.getString("post_likes"),//3
                                    json.getString("post_comments"),//4
                                    json.getString("post_shares"),//5
                                    json.getString("post_profile_img"),//6
                                    json.getString("post_profile_name"),//7
                                    json.getString("isLiked"),//8
                                    json.getString("post_date"),//9
                                    json.getString("by_user")//10
                            );
                            myPostListModelArrayList.add(course);
                        }
                        myPostLiveDataList.setValue(myPostListModelArrayList);

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
            }
        });

        return myPostLiveDataList;
    }
}
