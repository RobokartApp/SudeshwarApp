package com.ark.robokart_robotics.Activities.AtlChooseLevel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.AtlChooseStandard.AtlChooseStandard;
import com.ark.robokart_robotics.Activities.AtlCourseDetails;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class AtlChooseLevel extends AppCompatActivity {

    private TextView tvName,tvGood;
    ImageView back_btn;
    TextView t1;
    private static final String TAG = "BeginnerRepository";
    private RequestQueue requestQueue;
    String std="";
    public static String indx="";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atl_choose_level);
        std= AtlChooseStandard.std;
        init();
        listeners();
getAtl();
    }
    private void init(){

        progressBar=findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        tvName = findViewById(R.id.tvName);
        back_btn = findViewById(R.id.back_btn);
        tvGood = findViewById(R.id.tvGood);
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
    public void listeners() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final int[] ids={R.id.doc1,R.id.doc2,R.id.doc3,R.id.doc4,R.id.doc5,R.id.doc6,R.id.doc7,R.id.doc8,R.id.doc9,R.id.doc10,R.id.doc11,R.id.doc12,R.id.doc13,R.id.doc14,R.id.doc15};

        for(final int id:ids){
    findViewById(id).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            indx=""+v.getTag();
            Intent intent = new Intent(getApplicationContext(), AtlCourseDetails.class);
            startActivity(intent);
        }
    });
}
    }
   /* public static String[] prob_stat=new String[20],component=new String[20],procedure=new String[20],code=new String[20],
            title=new String[20],circuit=new String[20],output=new String[20];*/
    public static ArrayList<String> prob_stat=new ArrayList<>(),component=new ArrayList<>(),procedure=new ArrayList<>(),code=new ArrayList<>(),
    title=new ArrayList<>(),circuit=new ArrayList<>(),output=new ArrayList<>();
    public void getAtl(){
        StringRequest request = new StringRequest(Request.Method.GET, ApiConstants.HOST + ApiConstants.atl_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("atl");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
prob_stat.clear();component.clear();procedure.clear();code.clear();title.clear();circuit.clear();output.clear();
                if (status == 1) {
                    try{
                        int k=0;
                        for(int i = 0; i< atl.length();i++){
                            JSONObject json = atl.getJSONObject(i);
                            //Toast.makeText(this, ""+json.getString("std"), Toast.LENGTH_SHORT).show();
                                    if(json.getString("std").equals(std)){
                                        //Toast.makeText(this, "in std: "+std, Toast.LENGTH_SHORT).show();
                                        int[] ids={R.id.txt_doc1,R.id.txt_doc2,R.id.txt_doc3,R.id.txt_doc4,R.id.txt_doc5,R.id.txt_doc6,R.id.txt_doc7,R.id.txt_doc8,R.id.txt_doc9,R.id.txt_doc10,R.id.txt_doc11,R.id.txt_doc12,R.id.txt_doc13,R.id.txt_doc14,R.id.txt_doc15};

                                            t1=findViewById(ids[k]);
                                            t1.setText(std+"."+(k+1)+" "+json.getString("title"));
                                            title.add(json.getString("title"));
                                        component.add(json.getString("components"));
                                        prob_stat.add(json.getString("prob_stat"));
                                        procedure.add(json.getString("procedure"));
                                        circuit.add(json.getString("circuit"));
                                        output.add(json.getString("output"));
                                        code.add(json.getString("code"));

                                    }else {
                                        //Toast.makeText(this, "" +component[0], Toast.LENGTH_LONG).show();
                                        //break;
                                    }
                                    k++;
                                    if(k==15)k=0;
                        }


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
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}