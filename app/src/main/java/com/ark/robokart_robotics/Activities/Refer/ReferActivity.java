package com.ark.robokart_robotics.Activities.Refer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.SlidingImage_Adapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ReferActivity extends AppCompatActivity {

    TextView createlink,invited_users,invite_info;
    String cust_id,invited_count,invite_notes;
    String link="";
    ImageView back_ic;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        createlink = findViewById(R.id.createlink);
        invited_users=findViewById(R.id.invited_users);
        progressBar=findViewById(R.id.progressBar);
        invite_info=findViewById(R.id.invite_info);

         sharedPreferences= getSharedPreferences("userdetails",Context.MODE_PRIVATE);
        cust_id = sharedPreferences.getString("customer_id","848");
        link = sharedPreferences.getString("invite_link","NA");

        getInvitedUsers();

        Log.d("Value of link",link);
        createlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //createlink();
                if(link.equals("NA")||link.equals(""))
                    createlink();
                else
                {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage= "\nROBOKART has an awesome Referral Program! Use my referral link to earn coins:\n";
                    shareMessage = shareMessage + link;
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                    intent.setType("text/plain");
                    startActivity(intent);

                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        back_ic=findViewById(R.id.back_btn);

        back_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferActivity.super.onBackPressed();
            }
        });
    }
    public void createlink(){
        Log.e("main", "create link ");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://robokart.com/App?by="+cust_id))
                .setDynamicLinkDomain("robokart.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                //.setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
//click -- link -- google play store -- inistalled/ or not  ----
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "  Long refer "+ dynamicLink.getUri());
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link
        // manuall link
        String sharelinktext  = "https://robokart.page.link/?"+
                "link=https://robokart.com/App?by="+cust_id+
                "&apn="+ getPackageName()+
                "&st="+"My Refer Link"+
                "&sd="+"Reward Coins 20"+
                "&si="+"https://play-lh.googleusercontent.com/7Ed9yb154A8MiLE1R-lRpHBshff_TOxwyFQFGCnIAJlbDBR_NmvIpdrklGZe-Y39r2I=s150-rw";
        // shorten the link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                //.setLongLink(dynamicLink.getUri())
                .setLongLink(Uri.parse(sharelinktext))  // manually
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            progressBar.setVisibility(View.GONE);
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main ", "short link "+ shortLink.toString());
                            // share app dialog
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("invite_link", shortLink.toString());
                            editor.apply();
                            link=shortLink.toString();

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                            String shareMessage= "\nROBOKART has an awesome Referral Program! Use my referral link to earn coins:\n";
                            shareMessage = shareMessage + link;
                            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                            intent.setType("text/plain");
                            startActivity(intent);
                        } else {
                            // Error
                            // ...
                            Log.e("main", " error "+task.getException() );
                        }
                    }
                });
    }

    void getInvitedUsers(){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.get_invited_users, response -> {
            try {
                Log.d("HomeAct INvited",response);
                JSONObject jsonObject = new JSONObject(response);
                invited_count= jsonObject.getString("invited_users");
                invite_notes= jsonObject.getString("invite_notes");

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("User_id",cust_id);
                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                invited_users.setText("Invited Users : "+invited_count);
                invite_info.setText(invite_notes);
            }
        });

    }

}