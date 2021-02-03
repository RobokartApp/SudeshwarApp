package com.ark.robokart_robotics.Fragments.Stories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.CustomViewHolder > {

    public Context context;
    private final Home_Adapter.OnItemClickListener listener;
    private final ArrayList<VideoItem> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon,VideoItem item, View view);
    }



    public Home_Adapter(Context context, ArrayList<VideoItem> dataList, Home_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
       return dataList.size();
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        final VideoItem item= dataList.get(i);
        holder.setIsRecyclable(false);

        try {

        holder.bind(i,item,listener);


        }catch (Exception e){

        }
   }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView view_btn,like_btn,share_btn,comment_btn;

        LinearLayout user_info,lcs_right;
        TextView vidTitle;
        TextView like_count,view_count,profileName,comment_count;
        CircleImageView profileImg;

        public CustomViewHolder(View view) {
            super(view);

            view_btn=view.findViewById(R.id.view_btn_iv);
            view_btn.setEnabled(false);
            user_info=view.findViewById(R.id.user_info);
            lcs_right=view.findViewById(R.id.lnr_Right);

            like_btn=view.findViewById(R.id.like_btn_iv);
            comment_btn=view.findViewById(R.id.comment_btn_iv);
            share_btn=view.findViewById(R.id.share_btn_iv);

            like_count=itemView.findViewById(R.id.no_likes);
            comment_count=itemView.findViewById(R.id.no_comments);
            view_count=itemView.findViewById(R.id.no_videws);
            profileName=itemView.findViewById(R.id.profile_name_story);
            profileImg=itemView.findViewById(R.id.profile_img_story);

            vidTitle=view.findViewById(R.id.textVideoTitle);

        }

        public void bind(final int postion,final VideoItem item, final OnItemClickListener listener) {

            vidTitle.setText(dataList.get(postion).videoTitle);

            like_count.setText(dataList.get(postion).noLike);
            comment_count.setText(dataList.get(postion).noComment);
            view_count.setText(dataList.get(postion).noView);

            if(dataList.get(postion).isLike.equals("yes")){
                like_btn.setImageResource(R.drawable.heart);
                like_btn.setImageTintList(ContextCompat.getColorStateList(context, R.color.orange));
                like_btn.setEnabled(false);
            }

            String imgUrl="https://robokart.com/admin/assets/uploads/images/customer/"+dataList.get(postion).profileImg;
            Glide.with(context).load(imgUrl).disallowHardwareConfig().diskCacheStrategy(DiskCacheStrategy.DATA).into(profileImg);
            profileName.setText(dataList.get(postion).profileName);

            view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "view clicked"+dataList.get(postion).isLike, Toast.LENGTH_SHORT).show();
                }
            });

            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "liked "+dataList.get(postion).postId, Toast.LENGTH_SHORT).show();
                    like_btn.setImageResource(R.drawable.heart);
                    like_btn.setImageTintList(ContextCompat.getColorStateList(context, R.color.orange));
                    if(!like_count.getText().toString().equals(""))
                    {
                        int i=Integer.parseInt(like_count.getText().toString());
                        i++;
                        like_count.setText(""+i);
                    }else
                        like_count.setText("1");

                    like_btn.setEnabled(false);
                    sendLcs("like",dataList.get(postion).postId);
                }
            });
            comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               /*     Intent intent=new Intent(context, DoubtAllComment.class);
                    intent.putExtra("post_id",dataList.get(postion).postId);
                    intent.putExtra("story","ok");
                    context.startActivity(intent);
*/
                    listener.onItemClick(postion,item,view);
                }
            });
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                        String shareMessage= "\nFound this amazing story on Robokart app. Download the app for free and take a look at this:\n";
                        shareMessage = shareMessage + "https://robokart.com/app/Story?id="+dataList.get(postion).postId;
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        context.startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));

                    } catch(Exception e) {
                        //e.toString();
                    }
                }
            });
            vidTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            user_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            lcs_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            sendLcs("view",dataList.get(postion).postId);
        }


    }
    void sendLcs(String lcs,String postId) {
        Log.i("sending lcs","id:"+postId);
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.send_video_lcs, response -> {
            Log.d("like respo ",response);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Vid Adapter", "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("post_id",postId);
                parameters.put("user_id", StoriesFragment.user_id);
                parameters.put(lcs,"1");
                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                //finish();startActivity(getIntent());
            }
        });
    }


}