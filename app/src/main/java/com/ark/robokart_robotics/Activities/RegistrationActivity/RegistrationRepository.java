package com.ark.robokart_robotics.Activities.RegistrationActivity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RegistrationRepository {

    private RequestQueue requestQueue;

    public RegistrationRepository(Application application){
        requestQueue = Volley.newRequestQueue(application);
    }
}
