package com.ark.robokart_robotics.Activities.Profile;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileRepository {

    private static final String TAG = "ProfileRepository";

    private final Application application;

    private final RequestQueue requestQueue;

    private final MutableLiveData<String> message = new MutableLiveData<>();

    public ProfileRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<String> updateProfile(String User_id, String customer_name, String email, String mobile,
                                                 String password, String customer_image, String username,String bio){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.editprofile_api, response -> {
            try {
                Log.d("update respo",response+"\npass"+password);
                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");

                int status = jsonObject.getInt("statusId");

                String msg = result.getString("message");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "update: "+result.getString("message"));

                    SharedPref sharedPref = new SharedPref();
                    sharedPref.setUserDetails(application,User_id,customer_name,mobile,email,password,customer_image, username);

                    message.setValue(msg);

                }else if (status == 0) {
                    Log.d(TAG, "update: "+result.getString("message"));
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
                parameters.put("customer_name", customer_name);
                parameters.put("email", email);
                parameters.put("mobile", mobile);
                parameters.put("password", password);
                parameters.put("bio", bio);
                parameters.put("User_id",User_id);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }

}
