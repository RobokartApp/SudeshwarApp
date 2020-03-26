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

    private Application application;

    private RequestQueue requestQueue;

    private MutableLiveData<String> message = new MutableLiveData<>();

    public RegistrationRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }


    public MutableLiveData<String> register(String fullname, String email, String password, String refer_code, String student_number, String parent_number, String username){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.registration_api, response -> {
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
                parameters.put("customer_name", fullname);
                parameters.put("customer_email", email);
                parameters.put("customer_password", password);
                parameters.put("customer_code", refer_code);
                parameters.put("customer_mobile", student_number);
                parameters.put("customer_parents_number", parent_number);
                parameters.put("username", username);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }
}
