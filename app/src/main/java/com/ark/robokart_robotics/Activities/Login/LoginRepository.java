package com.ark.robokart_robotics.Activities.Login;

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
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginRepository {


    private static final String TAG = "LoginRepository";

    private final Application application;

    private final RequestQueue requestQueue;

    private final MutableLiveData<String> message = new MutableLiveData<>();

    private final MutableLiveData<String> email_message = new MutableLiveData<>();



    public LoginRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<String> requestOTP(String phone_number){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.login_otp, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");


                int status = jsonObject.getInt("statusId");

                String msg = result.getString("message");

                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "login: "+result.getString("message"));


                    message.setValue(msg);

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
                parameters.put("mobile", phone_number);
                parameters.put("otp","");
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }


    public MutableLiveData<String> loginwithEmail(String email, String password){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.login_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
Log.d(TAG,"Respo: "+response);
                JSONObject result = jsonObject.getJSONObject("result");



                int status = jsonObject.getInt("statusId");

                String msg = result.getString("message");

                if (status == 1) {
                    JSONObject userdetails = result.getJSONObject("userdetails");
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "login: "+result.getString("message"));

                    String cust_id = userdetails.getString("customer_id");
                    String fullname = userdetails.getString("customer_name");
                    String customer_email = userdetails.getString("customer_email");
                    String cust_mobile = userdetails.getString("customer_moblie");
                    String pass= userdetails.getString("password");
                    String cust_image = userdetails.getString("customer_image");
                    String user_name = userdetails.getString("username");

                    SharedPref sharedPref = new SharedPref();
                    sharedPref.setUserDetails(application,cust_id,fullname,cust_mobile,customer_email,pass,cust_image,user_name);
                    //FirebaseMessaging.getInstance().subscribeToTopic(customer_email);

                    message.setValue(msg);

                }else if (status == 0) {
                    Log.d(TAG, "login: "+result.getString("message"));
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
                parameters.put("email", email);
                parameters.put("password",password);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }

}
