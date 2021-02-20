package com.ark.robokart_robotics.Activities.AskDoubt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoubtAllComment extends AppCompatActivity {

    ImageView back_btn;
    String postId,cust_id;
    LinearLayout forComment;
    private RequestQueue requestQueue;
    ProgressBar progressBar,pbar_send;
    EditText comment_editTxt;
    ImageView send_btn;
    String apiUrl,getcmntUrl,destAct;
    String post_ques="";
    TextView ques_text;
    public static String delete_comment_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_all_comment);

        apiUrl=ApiConstants.send_comment_api;
        getcmntUrl=ApiConstants.post_comment_api;
        destAct="ok";
        init();
        listener();
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
        cust_id = sharedPreferences.getString("customer_id","");
        Intent intent = getIntent();
        postId=intent.getExtras().getString("post_id");
        post_ques=intent.getExtras().getString("post_ques");

        delete_comment_api="delete_doubt_comment.php";

        if(intent.getExtras().getString("story")!=null){
            Log.i("story log is","true");
            apiUrl=ApiConstants.send_video_lcs;
            getcmntUrl=ApiConstants.get_comment_story;
            destAct="story";
            delete_comment_api="delete_story_comment.php";
        }
        ques_text.setText(post_ques);
        getComments(postId,forComment);

    }

    private void init() {
        requestQueue = Volley.newRequestQueue(DoubtAllComment.this);
        back_btn=findViewById(R.id.iv_Back);
        forComment=findViewById(R.id.comment_layout);
        progressBar=findViewById(R.id.progressBar);
        pbar_send=findViewById(R.id.pbar_Send);
        pbar_send.setVisibility(View.GONE);
        ques_text=findViewById(R.id.ques_text);

        comment_editTxt=findViewById(R.id.post_comment_et);
        send_btn=findViewById(R.id.iv_Send);
    }

    private void listener() {
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(comment_editTxt.getText().toString().equals("")){
                    comment_editTxt.setError("Enter some text here");
                    comment_editTxt.requestFocus();
                }else{
                    sendComment(comment_editTxt.getText().toString());
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoubtAllComment.this.onBackPressed();
            }
        });
    }

    private void sendComment(String comment) {
        send_btn.setVisibility(View.GONE);
        pbar_send.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + apiUrl, response -> {
Log.d("comment respo ",response);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("post_id",postId);
                parameters.put("user_id",cust_id);
                parameters.put("comment",comment);
                return parameters;
            }
        };
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                send_btn.setVisibility(View.VISIBLE);
                pbar_send.setVisibility(View.GONE);
                //startActivity(getIntent());
                Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("comment", "res");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public void getComments(String post_id, LinearLayout forComment){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST +getcmntUrl , response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray atl = jsonObject.getJSONArray("comment");

                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                //Toast.makeText(DoubtAllComment.this, ""+response, Toast.LENGTH_LONG).show();

                if (status == 1) {
                    try{


                        for(int i = 0; i< atl.length();i++){

                            JSONObject json = atl.getJSONObject(i);
                            //ids.add(i,""+json.getString("id"));
                            String image=json.getString("profile_img");
                            String name=json.getString("profile_name");
                            String comment=json.getString("text");
                            String time=json.getString("c_time");
                            String by_user=json.getString("by_user");
                            String lcs_id=json.getString("lcs_id");
                            new addComment(DoubtAllComment.this, image, name, comment, time, forComment, by_user, lcs_id, post_id) {
                                @Override
                                void onDelete() {
                                    Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
                                    resultIntent.putExtra("comment", "minus");

                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }
                            };
                        }

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("post_id",post_id);
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

    @Override
    public void onBackPressed() {

            Intent intent = new Intent(DoubtAllComment.this, HomeActivity.class);
            intent.putExtra("post", destAct);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);

        finish();
    }
}