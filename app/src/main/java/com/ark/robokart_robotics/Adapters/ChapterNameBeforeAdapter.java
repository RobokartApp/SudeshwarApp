package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.CourseEnrolled.CourseEnrolledDetailsActivity;
import com.ark.robokart_robotics.Activities.CourseEnrolled.CourseEnrolledDetailsViewModel;
import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingViewModel;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChapterNameBeforeAdapter extends RecyclerView.Adapter<ChapterNameBeforeAdapter.MyViewHolder> {

    private static final String TAG = "ChapterAdapter";

    private List<Class_chapters> chapters;
    private Context mContext;
    int flag = 0;
    int open_flag = 0;
    private RequestQueue requestQueue;
    public String pos = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_chapter;
        public ImageView arrow, lock;
        public RecyclerView recyclerView;
        private ArrayList<Class_chapters.Course_List> mycourseListArrayList;

        public MyViewHolder(View view) {
            super(view);
            tv_chapter = view.findViewById(R.id.chapter_name);
            arrow = view.findViewById(R.id.arrow);
            lock = view.findViewById(R.id.lock);
            recyclerView = view.findViewById(R.id.projectsRecyclerview);
            mycourseListArrayList = new ArrayList<>();
        }
    }

    public ChapterNameBeforeAdapter(Context context, List<Class_chapters> chapters) {
        mContext = context;
        this.chapters = chapters;

        requestQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_courseinclusion_item, parent,false);




        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            final Class_chapters image = chapters.get(position);

            holder.tv_chapter.setText(image.getChapter_name());
            holder.mycourseListArrayList = image.getCourses();
            ChapterContentBeforeAdapter chapterContentBeforeAdapter = new ChapterContentBeforeAdapter(getApplicationContext(), holder.mycourseListArrayList);
            holder.recyclerView.setAdapter(chapterContentBeforeAdapter);


            holder.tv_chapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(flag == 0) {
                        flag = 1;
                        holder.recyclerView.animate().alpha(1.0f);
                        holder.recyclerView.setVisibility(View.VISIBLE);

                    }
                    else {
                        flag = 0;
                        holder.recyclerView.animate().alpha(0.0f);
                        holder.recyclerView.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception ex){
            Log.e(TAG,  ex.getMessage().toString());
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

}
