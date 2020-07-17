package com.ark.robokart_robotics.Activities.VideoPlaying;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.CommentModel;
import com.ark.robokart_robotics.Model.CurriculumModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoPlayingRepository {

    private static final String TAG = "VideoPlayingRepository";

    private Application application;

    private MutableLiveData<List<CurriculumModel>> curriculumList = new MutableLiveData<>();

    private ArrayList<CurriculumModel> curriculumModelArrayList = new ArrayList<>();

    private MutableLiveData<List<CommentModel>> commentList = new MutableLiveData<>();

    private ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();

    private MutableLiveData<String> message = new MutableLiveData<>();

    private RequestQueue requestQueue;


    public VideoPlayingRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<CurriculumModel>> getCurriculumList(String course_id){
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.getCurriculum_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray cur = jsonObject.getJSONArray("curr");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");

                if (status == 1) {
                    try{
                        for(int i = 0; i< cur.length();i++){
                            JSONObject json = cur.getJSONObject(i);
                            CurriculumModel curriculumModel = new CurriculumModel(
                                    json.getString("curriculum")
                            );
                            curriculumModelArrayList.add(curriculumModel);
                        }

                        curriculumList.setValue(curriculumModelArrayList);
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
                Log.d(TAG, "fetch: "+e.getMessage());

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
                parameters.put("course_id",course_id);
                return parameters;
            }
        };
        requestQueue.add(request);

        return  curriculumList;
    }


    public MutableLiveData<List<CommentModel>> getCommentList(String course_id, String customer_id){
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.getComments_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray comments = jsonObject.getJSONArray("comments");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");

                if (status == 1) {
                    try{
                        for(int i = 0; i< comments.length();i++){
                            JSONObject json = comments.getJSONObject(i);
                            CommentModel comment = new CommentModel(
                                    json.getString("comment_id"),
                                    json.getString("customer_name"),
                                    json.getString("customer_image"),
                                    json.getString("comment")
                            );
                            commentModelArrayList.add(comment);
                        }

                        commentList.setValue(commentModelArrayList);
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
                Log.d(TAG, "fetch: "+e.getMessage());

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
                parameters.put("course_id",course_id);
                parameters.put("customer_id",customer_id);
                return parameters;
            }
        };
        requestQueue.add(request);

        commentList.setValue(commentModelArrayList);
        return  commentList;
    }

    public MutableLiveData<String> postComment(String course_id, String chapter_id, String chapter_name, String comment, String customer_id){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.add_comment_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");

                int status = jsonObject.getInt("statusId");

                String msg = result.getString("message");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "registration: "+result.getString("message"));

                    message.setValue(msg);

                }else if (status == 0) {
                    Log.d(TAG, "registration: "+result.getString("message"));
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
                parameters.put("course_id", course_id);
                parameters.put("chapter_id", chapter_id);
                parameters.put("chapter_name", chapter_name);
                parameters.put("comment", comment);
                parameters.put("customer_id", customer_id);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }
}
