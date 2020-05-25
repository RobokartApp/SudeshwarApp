package com.ark.robokart_robotics.Activities.Collect_Recommendation;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
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
import com.ark.robokart_robotics.Model.Recommendations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectRecomRepository {

    private static final String TAG = "CollectRecomRepository";

    private Application application;

    private RequestQueue requestQueue;

    private MutableLiveData<List<Recommendations>> recommendationList = new MutableLiveData<>();

    private ArrayList<Recommendations> recommendationsArrayList = new ArrayList<>();

    private MutableLiveData<Integer> message = new MutableLiveData<>();

    public CollectRecomRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<Recommendations>> getRecommendationList(){
        recommendationsArrayList.add(new Recommendations("1","Quadcopter"));
        recommendationsArrayList.add(new Recommendations("2","AI"));
        recommendationsArrayList.add(new Recommendations("3","Robotics"));
        recommendationsArrayList.add(new Recommendations("4","3D Printing"));
        recommendationsArrayList.add(new Recommendations("5","Circuit Design"));
        recommendationsArrayList.add(new Recommendations("6","Circuit Design"));

        recommendationList.setValue(recommendationsArrayList);
        return recommendationList;
    }

    public MutableLiveData<Integer> collect(String r_id, String customer_id){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.local_HOST + ApiConstants.collect_recommendation_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                int status = jsonObject.getInt("success_code");

                int msg = jsonObject.getInt("recom_id");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    SharedPref sharedPref = new SharedPref();
                    sharedPref.setRecommendation(application,1);

                    message.setValue(msg);

                }else if (status == 0) {
                    Log.d(TAG, "collect: "+jsonObject.getString("error_msg"));
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
                parameters.put("r_id", r_id);
                parameters.put("customer_id", customer_id);
                parameters.put("customer_std","");
                parameters.put("customer_school_code","");
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }

}
