package com.ark.robokart_robotics.Activities.RegistrationActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationRepository {

    private static final String TAG = "RegistrationRepository";

    private final Application application;

    private final RequestQueue requestQueue;

    private final MutableLiveData<String> message = new MutableLiveData<>();

    public RegistrationRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }


    public MutableLiveData<String> register(String childName, String parentName, String mobile, String parentEmail, String grade, String havePc){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.registration_api, response -> {
            try {
                Log.d("reg Respo",response);

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
                    message.setValue(msg);
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
                parameters.put("customer_name", childName);
                parameters.put("customer_email", parentEmail);
                parameters.put("customer_mobile", mobile);
                parameters.put("parent_name", parentName);
                parameters.put("grade", grade);
                parameters.put("have_pc", havePc);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }
}
