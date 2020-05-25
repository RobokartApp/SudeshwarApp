package com.ark.robokart_robotics.Fragments.Courses;

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
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.MyCoursesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesRepository {

    private static final String TAG = "CoursesRepository";

    private Application application;

    private RequestQueue requestQueue;

    private MutableLiveData<List<MyCoursesModel>> myCoursesLiveDataList = new MutableLiveData<>();

    private ArrayList<MyCoursesModel> mycourseListModelArrayList = new ArrayList<>();

    public CoursesRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }


    public MutableLiveData<List<MyCoursesModel>> getMyCoursesList(String customer_id){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.mycourses_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("user_courses");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");

                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            MyCoursesModel course = new MyCoursesModel(
                                    json.getString("course_name"),
                                    json.getString("course_video_thumb"),
                                    json.getString("learn_percent")
                            );
                            mycourseListModelArrayList.add(course);
                        }
                        myCoursesLiveDataList.setValue(mycourseListModelArrayList);

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
                parameters.put("customer_id","836");
                return parameters;
            }
        };
        requestQueue.add(request);

        return myCoursesLiveDataList;
    }

}
