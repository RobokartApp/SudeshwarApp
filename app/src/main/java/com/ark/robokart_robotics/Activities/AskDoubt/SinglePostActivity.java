package com.ark.robokart_robotics.Activities.AskDoubt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.PostAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.MyPostModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinglePostActivity extends AppCompatActivity {


    ImageView postImg,dot_btn,back_btn,error_img;
    TextView postTitle,like_count,comment_count,share_count,profileName,postDate;
    FloatingActionButton like,share,comment;
    CircleImageView profileImg;
    Context mContext;

    String getPost_profile_name,getPost_title,getPost_like,getPost_comment,getPost_share,
            getPost_profile_img,getPost_id,getPost_img,getPost_date,getIsLiked,getBy_user;

    private RequestQueue requestQueue;
    String cust_id,post_id;
    Button report_spam,report_nudity;

    ProgressBar progressBar;
    boolean from=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        Log.i("SingleAct","came to single post act");

        init();


        Bundle bundle=getIntent().getExtras();

        if(getIntent().hasExtra("post_id")){
            post_id=bundle.getString("post_id");
            Log.i("SinglePostAct","post_id:"+post_id);
        }else {
            post_id = "1";
            Log.i("SinglePostAct","no bundle no extras");
        }

        from=getIntent().hasExtra("from");

        getPost();

    }

    private void init() {

        mContext=this;

        error_img=findViewById(R.id.error_img);

        requestQueue = Volley.newRequestQueue(mContext);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userdetails",MODE_PRIVATE);
        cust_id = sharedPreferences.getString("customer_id","848");

        progressBar=findViewById(R.id.progressBar);
        back_btn=findViewById(R.id.back_btn);
        postImg=findViewById(R.id.post_image);
        postTitle=findViewById(R.id.post_title);
        like=findViewById(R.id.post_like_btn);
        share=findViewById(R.id.post_share_btn);
        comment=findViewById(R.id.post_comment_btn);
        like_count=findViewById(R.id.post_count_like);
        comment_count=findViewById(R.id.post_count_comment);
        share_count=findViewById(R.id.post_count_share);
        profileImg=findViewById(R.id.post_profile_Pic);
        profileName=findViewById(R.id.post_profile_name);
        dot_btn=findViewById(R.id.dot_btn);
        postDate=findViewById(R.id.post_date);
    }

    private void listener() {
        String imgUrl="https://robokart.com/admin/assets/uploads/images/customer/";
        String postImgUrl="https://robokart.com/app/images/posts/";
        if(getPost_img.equals("NA"))
            postImg.setVisibility(View.GONE);
        else {
            postImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(postImgUrl + getPost_img).into(postImg);
        }
        if (getPost_img.equals("null"))
            postImg.setImageResource(R.mipmap.robokart_logo);

        Glide.with(mContext).load(imgUrl+getPost_profile_img).into(profileImg);

        profileName.setText(getPost_profile_name);
        postTitle.setText(getPost_title);
        like_count.setText(getPost_like);

        comment_count.setText(getPost_comment);
        share_count.setText(getPost_share);

        postDate.setText(getPost_date);

        if(getIsLiked.equals("yes")){
            like.setImageResource(R.drawable.heart);
            like.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            like.setEnabled(false);
        }

        error_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(mContext);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.preview_img);
                Button btnClose = nagDialog.findViewById(R.id.btnIvClose);
                ProgressBar progressBar=nagDialog.findViewById(R.id.progressBar);
                ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
                Glide.with(mContext).load(postImgUrl+getPost_img)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .disallowHardwareConfig().into(ivPreview);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                nagDialog.setCanceledOnTouchOutside(true);
                nagDialog.show();
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "id"+mpostList.get(position).getPost_id(), Toast.LENGTH_SHORT).show();
                like.setImageResource(R.drawable.heart);
                like.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                if(!like_count.getText().toString().equals(""))
                {
                    int i=Integer.parseInt(like_count.getText().toString());
                    i++;
                    like_count.setText(""+i);
                }else
                    like_count.setText("1");

                like.setEnabled(false);
                sendLike("1",getPost_id);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "commenting...", Toast.LENGTH_SHORT).show();
                //holder.comment.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.purple));
                Intent intent=new Intent(mContext, DoubtAllComment.class);
                intent.putExtra("post_id",getPost_id);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "sharing...", Toast.LENGTH_SHORT).show();
                //holder.share.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.green));
                if(!share_count.getText().toString().equals(""))
                {
                    int i=Integer.parseInt(share_count.getText().toString());
                    i++;
                    share_count.setText(""+i);
                }else
                    share_count.setText("1");
                sendShare(getPost_id);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage= "\nFound this doubt on Robokart app. Help to find the solution for it:\n";
                    shareMessage = shareMessage + "https://robokart.com/app/Ask_Doubt?id="+post_id;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    mContext.startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));

                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        dot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showCustomDialog(getPost_id);
                //Toast.makeText(mContext, "byuser:"+getBy_user+"&custID:"+cust_id, Toast.LENGTH_SHORT).show();
                if(!getBy_user.equals(cust_id))
                    showCustomDialog(getPost_id);
                else
                    showDeleteDialog(getPost_id);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from)
                    onBackPressed();
                else {
                    finish();
                    Intent intent = new Intent(SinglePostActivity.this, HomeActivity.class);
                    intent.putExtra("post", "ok");
                    startActivity(intent);
                }
            }
        });
    }

    private void showCustomDialog(String postId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        //ViewGroup viewGroup = mContext.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.my_dialog, null, false);

        report_spam=dialogView.findViewById(R.id.button_report);
        report_nudity=dialogView.findViewById(R.id.button_nudity);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        report_spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport("Spam report",postId);
                alertDialog.dismiss();

            }
        });
        report_nudity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport("Prommetes nudity",postId);
                alertDialog.dismiss();
            }
        });
    }

    private void showDeleteDialog(String postId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        //ViewGroup viewGroup = mContext.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog, null, false);

        Button delete_post=dialogView.findViewById(R.id.button_delete);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                deletePost(postId);
                                alertDialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                //finish();
                            }
                        })
                        .show();
                //deletePost(postId);
                //alertDialog.dismiss();

            }
        });

    }

    private void deletePost(String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "delete_doubt_api.php", response -> {
            Log.d("delete post respo ",response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("success_code");
                if (status == 1) {
                    onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("respo ask",response);

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
                return parameters;
            }
        };
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));;
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
                Toast.makeText(mContext, "Your post has been deleted!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendLike(String like,String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.send_comment_api, response -> {
            Log.d("like respo ",response);
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
                parameters.put("like",like);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
            }
        });
    }

    private void sendShare(String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.send_comment_api, response -> {
            Log.d("share respo ",response);
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
                parameters.put("share","share");
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
            }
        });
    }

    private void sendReport(String abuse,String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://robokart.com/webs/devaReport.php", response -> {
            Log.d("report respo ",response);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d( "Volley error: ",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("post_id",postId);
                parameters.put("user_id",cust_id);
                parameters.put("report",abuse);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
                Toast.makeText(mContext, "Report has been submitted!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getPost(){
        String TAG="Single PostAct";
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.single_doubt_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("ask_doubt");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo ask",response);
                if (status == 1) {
                    try{

                            JSONObject json = courses.getJSONObject(0);

                                    getPost_id=json.getString("post_id");
                                    getPost_img=json.getString("post_img");
                                    getPost_title=json.getString("post_title");
                                    getPost_like=json.getString("post_likes");
                                    getPost_comment=json.getString("post_comments");
                                    getPost_share=json.getString("post_shares");
                                    getPost_profile_img=json.getString("post_profile_img");
                                    getPost_profile_name=json.getString("post_profile_name");
                                    getIsLiked=json.getString("isLiked");
                                    getPost_date=json.getString("post_date");
                                    getBy_user=json.getString("by_user");


                    }
                    catch (Exception e){
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else if (status == 0) {
                    error_img.setVisibility(View.VISIBLE);
                    getPost_id="NA";
                    getPost_img="NA";
                    getPost_like="NA";
                    getPost_comment="NA";
                    getPost_share="NA";
                    getPost_profile_img="null";
                    getPost_profile_name="NA";
                    getIsLiked="NA";
                    getPost_date="NA";
                    getBy_user="NA";
                    getPost_title="This post has been deleted!";
                    Toast.makeText(getApplicationContext(), "No data found!", Toast.LENGTH_SHORT).show();
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
                parameters.put("customer_id",cust_id);
                parameters.put("post_id",post_id);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);
                listener();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (from)
            super.onBackPressed();
        else {
            finish();
            Intent intent = new Intent(SinglePostActivity.this, HomeActivity.class);
            intent.putExtra("post", "ok");
            startActivity(intent);
        }
    }
}