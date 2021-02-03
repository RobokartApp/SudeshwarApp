package com.ark.robokart_robotics.Activities.CourseDetails;

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
import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.Model.CourseInclusionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseInclusionReposiory {

    private static final String TAG = "CourseInclusionReposior";

    private final Application application;

    private final RequestQueue requestQueue;

    private final MutableLiveData<String> message = new MutableLiveData<>();

    private final MutableLiveData<List<String>> courseDetails = new MutableLiveData<>();

    private final ArrayList<String> courseDetailsList = new ArrayList<>();

    private final MutableLiveData<List<CourseInclusionModel>> courseInclusionList = new MutableLiveData<>();

    private final ArrayList<CourseInclusionModel> courseInclusionMArrayList = new ArrayList<>();

    private final MutableLiveData<List<Class_chapters>> chapterNameMutableLiveData = new MutableLiveData<>();


    private final ArrayList<Class_chapters> chaptersArrayList = new ArrayList<>();

    public CourseInclusionReposiory(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<CourseInclusionModel>> getCourseInclusionList(String courseid){
       StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.curriculaim_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONArray courses = result.getJSONArray("courses");
                int status = jsonObject.getInt("statusId");

                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            CourseInclusionModel course = new CourseInclusionModel(
                                    i,
                                    json.getString("chapter_name"));
                            courseInclusionMArrayList.add(course);
                        }
                        courseInclusionList.setValue(courseInclusionMArrayList);

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
        courseInclusionList.setValue(courseInclusionMArrayList);
        return courseInclusionList;
    }



    public MutableLiveData<List<Class_chapters>> getChapterName(String courseid){
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.courseContent_Api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONArray chaptername = result.getJSONArray("chaptername");
                int status = jsonObject.getInt("statusId");

                if (status == 1) {
                    try{
                        for(int i = 0; i< chaptername.length();i++){
                            JSONObject json = chaptername.getJSONObject(i);

                            JSONArray courses1 = json.getJSONArray("courses");

                            ArrayList<Class_chapters.Course_List> courseListArrayList = new ArrayList<>();
                            for(int j = 0; j < courses1.length(); j++){
                                JSONObject jsonObject1 = courses1.getJSONObject(j);
                                Class_chapters.Course_List course = new Class_chapters.Course_List(
                                        i,
                                        jsonObject1.getString("chapter_content"),
                                        jsonObject1.getString("video_time"),
                                        jsonObject1.getString("video_url"),
                                        jsonObject1.getString("assignment_url"),
                                        jsonObject1.getString("quiz_id")
                                );

                                courseListArrayList.add(course);
                                Log.d(TAG, "course: "+jsonObject1.getString("chapter_content"));
                            }

                            Class_chapters chapter = new Class_chapters(
                                    i,
                                    json.getString("chapter_name"),
                                    courseListArrayList);
                            chaptersArrayList.add(chapter);


                            Log.d(TAG, "----------------------------------------");

                        }

                        chapterNameMutableLiveData.setValue(chaptersArrayList);
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

    public MutableLiveData<String> getCourseAccess(String course_id, String customer_id){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.courseAccessApi, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");

                int status = jsonObject.getInt("statusId");

                String msg = result.getString("chapter_completed");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "course access: "+result.getString("chapter_completed"));

                    message.setValue(msg);

                }else if (status == 0) {
                    Log.d(TAG, "course access: "+result.getString("chapter_completed"));
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
                parameters.put("customer_id", customer_id);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }

}
