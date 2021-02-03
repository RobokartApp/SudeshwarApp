package com.ark.robokart_robotics.Activities.ChooseStandard;

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
import com.ark.robokart_robotics.Model.StandardModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardRepository {

    private static final String TAG = "StandardRepository";

    private final Application application;

    private final RequestQueue requestQueue;

    private final MutableLiveData<List<StandardModel>> standardList = new MutableLiveData<>();

    private final ArrayList<StandardModel> standardArrayList = new ArrayList<>();

    private final MutableLiveData<Integer> message = new MutableLiveData<>();

    public StandardRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<StandardModel>> getStandardList() {
        standardArrayList.add(new StandardModel("1", "VI"));
        standardArrayList.add(new StandardModel("2", "VII"));
        standardArrayList.add(new StandardModel("3", "VIII"));
        standardArrayList.add(new StandardModel("4", "IX"));
        standardArrayList.add(new StandardModel("5", "X"));
        standardArrayList.add(new StandardModel("6", "XI"));
        standardArrayList.add(new StandardModel("7", "XII"));
        standardList.setValue(standardArrayList);
        return standardList;
    }


    public MutableLiveData<Integer> selectStandard(String customer_id, String customer_std, String customer_school_code){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.standard_selection_api, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);

                JSONObject result = jsonObject.getJSONObject("result");

                int status = jsonObject.getInt("statusId");


                if (status == 1) {
                    //Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    SharedPref sharedPref = new SharedPref();
                    sharedPref.setStandardSelection(application,1);
                    message.setValue(status);

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
                parameters.put("r_id", "");
                parameters.put("customer_id", customer_id);
                parameters.put("customer_std",customer_std);
                parameters.put("customer_school_code",customer_school_code);
                return parameters;
            }
        };
        requestQueue.add(request);

        return message;
    }


}
