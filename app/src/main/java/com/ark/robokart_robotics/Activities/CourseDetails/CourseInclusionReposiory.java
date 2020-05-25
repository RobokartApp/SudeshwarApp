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
import com.ark.robokart_robotics.Common.SharedPref;
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

    private Application application;

    private RequestQueue requestQueue;

    private MutableLiveData<List<String>> courseDetails = new MutableLiveData<>();

    private ArrayList<String> courseDetailsList = new ArrayList<>();

    private MutableLiveData<List<CourseInclusionModel>> courseInclusionList = new MutableLiveData<>();

    private ArrayList<CourseInclusionModel> courseInclusionMArrayList = new ArrayList<>();

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

    public MutableLiveData<List<String>> getCourseDetails(String courseid){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.login_otp, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");

                JSONObject course_details = result.getJSONObject("course_details");

                int status = jsonObject.getInt("statusId");

                String msg = result.getString("message");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "course: "+result.getString("message"));

                    String course_name = course_details.getString("course_name");
                    String customer_rating = course_details.getString("customer_rating");
                    String course_enrolled = course_details.getString("course_enrolled");
                    String course_video_thumb = course_details.getString("course_video_thumb");
                    String online_price_title = course_details.getString("online_price_title");
                    String course_online_price = course_details.getString("course_online_price");
                    String offline_price_title = course_details.getString("offline_price_title");
                    String course_offline_price = course_details.getString("course_offline_price");

                    courseDetailsList.add(course_name);
                    courseDetailsList.add(customer_rating);
                    courseDetailsList.add(course_enrolled);
                    courseDetailsList.add(course_video_thumb);
                    courseDetailsList.add(online_price_title);
                    courseDetailsList.add(course_online_price);
                    courseDetailsList.add(offline_price_title);
                    courseDetailsList.add(course_offline_price);

                   courseDetails.postValue(courseDetailsList);

                }else if (status == 0) {
                    Log.d(TAG, "login: "+result.getString("message"));
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
                parameters.put("courseid", courseid);
                return parameters;
            }
        };
        requestQueue.add(request);

        return courseDetails;
    }

}
