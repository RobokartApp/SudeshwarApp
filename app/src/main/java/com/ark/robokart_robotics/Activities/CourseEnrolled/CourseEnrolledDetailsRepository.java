package com.ark.robokart_robotics.Activities.CourseEnrolled;

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
import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.ChapterName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseEnrolledDetailsRepository {

    private static final String TAG = "CourseEnrolledDetailsRe";

    private Application application;

    private RequestQueue requestQueue;

    private MutableLiveData<List<ChapterName>> chapterNameMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<ChapterContent>> chapterContentMutableLiveData = new MutableLiveData<>();

    private ArrayList<ChapterName> chapterNameArrayList = new ArrayList<>();

    private ArrayList<ChapterContent> chapterContentArrayList = new ArrayList<>();

    public CourseEnrolledDetailsRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<ChapterName>> getChapterName(String courseid){
        StringRequest request = new StringRequest(Request.Method.POST, "https://robokart.com/Api/courseContent_Api.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONArray courses = result.getJSONArray("courses");
                JSONArray chaptername = result.getJSONArray("chaptername");
                int status = jsonObject.getInt("statusId");

                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            ChapterContent course = new ChapterContent(
                                    json.getString("chapter_content"),
                                    json.getString("video_time"),
                                    json.getString("video_url"),
                                    json.getString("assignment_url"),
                                    json.getString("quiz_id")
                            );
                            chapterContentArrayList.add(course);
                        }



                        for(int i = 0; i< chaptername.length();i++){
                            JSONObject json = chaptername.getJSONObject(i);
                            ChapterName chapterName = new ChapterName(
                                    json.getString("chapter_name"),
                                    chapterContentArrayList);
                            chapterNameArrayList.add(chapterName);
                        }

                        chapterNameMutableLiveData.setValue(chapterNameArrayList);

                    }
                    catch (Exception e){
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "getCourseInclusionList: "+e.getMessage());
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "getCourseInclusion: "+e.getMessage());

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
                parameters.put("courseid",courseid);
                return parameters;
            }
        };
        requestQueue.add(request);

        return chapterNameMutableLiveData;
    }


    public MutableLiveData<List<ChapterContent>> getChapterContent(String courseid){
        StringRequest request = new StringRequest(Request.Method.POST, "https://robokart.com/Api/courseContent_Api.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONArray courses = result.getJSONArray("courses");
                JSONArray chaptername = result.getJSONArray("chaptername");
                int status = jsonObject.getInt("statusId");

                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            ChapterContent course = new ChapterContent(
                                    json.getString("chapter_content"),
                                    json.getString("video_time"),
                                    json.getString("video_url"),
                                    json.getString("assignment_url"),
                                    json.getString("quiz_id")
                            );
                            chapterContentArrayList.add(course);
                        }



                        for(int i = 0; i< chaptername.length();i++){
                            JSONObject json = chaptername.getJSONObject(i);
                            ChapterName chapterName = new ChapterName(
                                    json.getString("chapter_name"),
                                    chapterContentArrayList);
                            chapterNameArrayList.add(chapterName);
                        }

                    }
                    catch (Exception e){
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "getCourseInclusionList: "+e.getMessage());
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "getCourseInclusion: "+e.getMessage());

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
                parameters.put("courseid",courseid);
                return parameters;
            }
        };
        requestQueue.add(request);

        return chapterContentMutableLiveData;
    }

}
