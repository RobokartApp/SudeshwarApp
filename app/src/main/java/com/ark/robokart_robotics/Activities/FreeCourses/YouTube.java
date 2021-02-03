package com.ark.robokart_robotics.Activities.FreeCourses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class YouTube extends AppCompatActivity {


    private TextView tvName,tvGood;
    ImageView back_btn;
    Context context;
    //PopularRecycler adapter;
    private RequestQueue requestQueue;
    LinearLayout forCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        //youTubePlayerView=findViewById(R.id.youtube);
        context=this;
        requestQueue = Volley.newRequestQueue(this);
        forCategory=findViewById(R.id.youtube_cat_lin);

        init();
        listeners();

    }

    private void listeners() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {

        tvName = findViewById(R.id.tvName);
        back_btn = findViewById(R.id.back_btn);
        tvGood = findViewById(R.id.tvGood);


        getIds();



        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            String fullname = sharedPreferences.getString("fullname","-");

            String lastName = "";
            String firstName= "";
            if(fullname.split("\\w+").length>1){

                lastName = fullname.substring(fullname.lastIndexOf(" ")+1);
                firstName = fullname.substring(0, fullname.lastIndexOf(' '));
            }
            else{
                firstName = fullname;
            }

            tvName.setText(firstName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        //Set greeting
        String greeting = null;
        if(hour>=6 && hour<12){
            greeting = "Good Morning,";
        } else if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon,";
        } else if(hour >= 17 && hour < 24){
            greeting = "Good Evening,";
        }
        else if(hour >= 0 && hour < 4){
            greeting = "Good Night,";
        }

        tvGood.setText(greeting);
    }

    public void getIds(){

        ArrayList<String> youtube_cats=new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "youtubeCategories.php", response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                //JSONArray atl = jsonObject.getJSONArray("youtube");
                JSONArray categories = jsonObject.getJSONArray("categories");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                //Toast.makeText(context, ""+categories, Toast.LENGTH_LONG).show();
                youtube_cats.clear();

                if (status == 1) {
                    try{
                        for(int i = 0; i< categories.length();i++) {
                            //Log.d("cats",""+categories.getString(i));
                            youtube_cats.add(categories.getString(i));
                        }
/*
                        for(int i = 0; i< atl.length();i++){

                            JSONObject json = atl.getJSONObject(i);
                            ids.add(i,""+json.getString("id"));
                            title.add(i,""+json.getString("title"));

                        }

 */

                    }
                    catch (Exception e){
                        Log.e("json getting data",e.getMessage());
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                //Log.d(TAG, "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }) /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("category",category);
                return parameters;
            }
        }*/
        ;
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //adapter=new PopularRecycler(context,ids,title);
               // recyclerView.setAdapter(adapter);
for(String head:youtube_cats){
    new addCategory(YouTube.this,forCategory,head);
}
            }
        });
    }
}