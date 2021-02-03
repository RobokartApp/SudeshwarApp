package com.ark.robokart_robotics.Fragments.Payment.BuyNow;

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

public class BuyNowRepository {

    private static final String TAG = "BuyNowRepository";

    private final RequestQueue requestQueue;

    private final Application application;

    private final MutableLiveData<String> message = new MutableLiveData<>();

    public BuyNowRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<String> checkLicenseKey(String login_id, String course_id, String code){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.license_key_offlineApi, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                Log.d(TAG,"Respo: "+response);
                String success = jsonObject.getString("Success");

                String content = jsonObject.getString("Content");

                if (success.equals("true")) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "course: "+content);

                    message.setValue(content);


                }else if (success.equals("false")) {
                    Log.d(TAG, "course: "+content);
                    message.setValue(content);
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
                parameters.put("login_id",login_id);
                parameters.put("course_id", course_id);
                parameters.put("code",code);
                return parameters;
            }
        };
        requestQueue.add(request);


        return message;
    }

}
