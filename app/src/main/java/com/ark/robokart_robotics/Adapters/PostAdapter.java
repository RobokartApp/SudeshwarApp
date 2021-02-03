package com.ark.robokart_robotics.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.AskDoubt.DoubtAllComment;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.MyPostModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.CustomHolder> {
    private final List<MyPostModel> mpostList;
    private final Context mContext;
    private final RequestQueue requestQueue;
    String cust_id;
    Button report_spam,report_nudity;

    public PostAdapter(Context context, List<MyPostModel> postListModelList) {

        this.mContext = context;
        this.mpostList = postListModelList;

        requestQueue = Volley.newRequestQueue(context);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userdetails",MODE_PRIVATE);
        cust_id = sharedPreferences.getString("customer_id","848");
    }


    @NonNull
    @Override
    public PostAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_post,parent,false);
        return new PostAdapter.CustomHolder(itemView);
    }


    public class CustomHolder extends RecyclerView.ViewHolder{
        ImageView postImg,dot_btn;
        TextView postTitle,like_count,comment_count,share_count,profileName,postDate;
        FloatingActionButton like,share,comment;
        CircleImageView profileImg;


        public CustomHolder(@NonNull View itemView) {
            super(itemView);
postImg=itemView.findViewById(R.id.post_image);
postTitle=itemView.findViewById(R.id.post_title);
like=itemView.findViewById(R.id.post_like_btn);
share=itemView.findViewById(R.id.post_share_btn);
comment=itemView.findViewById(R.id.post_comment_btn);
like_count=itemView.findViewById(R.id.post_count_like);
comment_count=itemView.findViewById(R.id.post_count_comment);
share_count=itemView.findViewById(R.id.post_count_share);
profileImg=itemView.findViewById(R.id.post_profile_Pic);
profileName=itemView.findViewById(R.id.post_profile_name);
dot_btn=itemView.findViewById(R.id.dot_btn);
postDate=itemView.findViewById(R.id.post_date);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.CustomHolder holder, int position) {
        //MyPostModel answers = mcourseList.get(position);
        String imgUrl="https://robokart.com/admin/assets/uploads/images/customer/";
        String postImgUrl="https://robokart.com/app/images/posts/";
        if(mpostList.get(position).getPost_img().equals("NA"))
            holder.postImg.setVisibility(View.GONE);
        else {
            holder.postImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(postImgUrl + mpostList.get(position).getPost_img()).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.postImg);
        }
        Glide.with(mContext).load(imgUrl+mpostList.get(position).getPost_profile_img()).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.profileImg);

        holder.profileName.setText(mpostList.get(position).getPost_profile_name());
        holder.postTitle.setText(mpostList.get(position).getPost_title());
        holder.like_count.setText(mpostList.get(position).getPost_like());

        holder.comment_count.setText(mpostList.get(position).getPost_comment());
        holder.share_count.setText(mpostList.get(position).getPost_share());

        holder.postDate.setText(mpostList.get(position).getPost_date());

        if(mpostList.get(position).getIsLiked().equals("yes")){
            holder.like.setImageResource(R.drawable.heart);
            holder.like.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            holder.like.setEnabled(false);
        }

        holder.postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(mContext);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.preview_img);
                Button btnClose = nagDialog.findViewById(R.id.btnIvClose);
                ProgressBar progressBar=nagDialog.findViewById(R.id.progressBar);
                ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
                Glide.with(mContext).load(postImgUrl+mpostList.get(position).getPost_img())
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

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "id"+mpostList.get(position).getPost_id(), Toast.LENGTH_SHORT).show();
                holder.like.setImageResource(R.drawable.heart);
                holder.like.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                if(!holder.like_count.getText().toString().equals(""))
                {
                    int i=Integer.parseInt(holder.like_count.getText().toString());
                    i++;
                    holder.like_count.setText(""+i);
                }else
                    holder.like_count.setText("1");

                holder.like.setEnabled(false);
                sendLike("1",mpostList.get(position).getPost_id());
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "commenting...", Toast.LENGTH_SHORT).show();
                //holder.comment.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.purple));
                Intent intent=new Intent(mContext, DoubtAllComment.class);
                intent.putExtra("post_id",mpostList.get(position).getPost_id());
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "sharing...", Toast.LENGTH_SHORT).show();
                //holder.share.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.green));
                if(!holder.share_count.getText().toString().equals(""))
                {
                    int i=Integer.parseInt(holder.share_count.getText().toString());
                    i++;
                    holder.share_count.setText(""+i);
                }else
                    holder.share_count.setText("1");
                sendShare(mpostList.get(position).getPost_id());
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage= "\nFound this doubt on Robokart app. Help to find the solution for it:\n";
                    shareMessage = shareMessage + "https://robokart.com/app/Ask_Doubt?id="+mpostList.get(position).getPost_id();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    mContext.startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));

                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        holder.dot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(mpostList.get(position).getPost_id());
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

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return mpostList.size();
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
}
