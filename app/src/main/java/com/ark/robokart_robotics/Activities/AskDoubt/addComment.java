package com.ark.robokart_robotics.Activities.AskDoubt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

abstract public class addComment {
    Context context;
    String image,name,comment,time,by_user,lcs_id,cust_id,Post_Id;
    LinearLayout forComment;

    abstract void onDelete();
    public addComment(Context context, String image, String name, String comment, String time, LinearLayout forComment,String by_user,String lcs_id,String Post_Id){
        this.context=context;
        this.by_user=by_user;
        this.lcs_id=lcs_id;
        this.comment=comment;
        this.image=image;
        this.name=name;
        this.Post_Id=Post_Id;
        this.time=time;
        this.forComment=forComment;

        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout comment_layout= (RelativeLayout) vi.inflate(R.layout.item_view_comment, null);

        SharedPreferences sharedPreferences = context.getSharedPreferences("userdetails",MODE_PRIVATE);
        cust_id = sharedPreferences.getString("customer_id","");

        String imgUrl="https://robokart.com/admin/assets/uploads/images/customer/";

        CircleImageView civ_image=comment_layout.findViewById(R.id.civ_Picture);
        TextView c_name=comment_layout.findViewById(R.id.commenter_name);
        TextView c_text=comment_layout.findViewById(R.id.comment_txt);
        TextView c_time=comment_layout.findViewById(R.id.comment_time);
        ImageView more_option=comment_layout.findViewById(R.id.more_options_iv);
        more_option.setVisibility(View.GONE);
        if (cust_id.equals(by_user))
            more_option.setVisibility(View.VISIBLE);

        more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(lcs_id,comment_layout);
            }
        });

        Glide.with(context).load(imgUrl+image).disallowHardwareConfig().into(civ_image);
        c_name.setText(name);
        c_text.setText(comment);
        c_time.setText(time);

        forComment.addView(comment_layout);

    }
    private void showDeleteDialog(String postId,RelativeLayout relativeLayout) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        //ViewGroup viewGroup = mContext.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null, false);

        Button delete_post=dialogView.findViewById(R.id.button_delete);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                deletePost(postId,relativeLayout);
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

    private void deletePost(String lcsId,RelativeLayout relativeLayout) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + DoubtAllComment.delete_comment_api, response -> {
            Log.d("delete comment respo ",response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("success_code");
                if (status == 1) {
                    forComment.removeView(relativeLayout);
                    onDelete();
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
                parameters.put("lcs_id",lcsId);
                parameters.put("post_id",Post_Id);
                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));;
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
                Toast.makeText(context, "Your post has been deleted!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
