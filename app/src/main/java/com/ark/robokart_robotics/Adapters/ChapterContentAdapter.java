package com.ark.robokart_robotics.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ark.robokart_robotics.Activities.CourseEnrolled.CourseEnrolledDetailsActivity;
import com.ark.robokart_robotics.Activities.CourseEnrolled.CourseEnrolledDetailsRepository;
import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingActivity;
import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.ChapterName;
import com.ark.robokart_robotics.R;
import com.artjimlop.altex.AltexImageDownloader;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static java.security.AccessController.getContext;

public class ChapterContentAdapter extends ExpandableRecyclerViewAdapter<ChapterNameViewHolder, ChapterContentViewHolder>{

    public Context mContext;

    private OnItemClickListener listener;

    public int total_number_chapter = 0;

    private ArrayList<ChapterContent> chapterContentList = new ArrayList<>();
    private ArrayList<ChapterName> chapterNameArrayList = new ArrayList<>();


    public ChapterContentAdapter(Context context,List<? extends ExpandableGroup> groups) {
        super(groups);
        this.mContext = context;
    }

    @Override
    public ChapterNameViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chapter_name_item,parent,false);
        return new ChapterNameViewHolder(mContext, v);
    }

    @Override
    public ChapterContentViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chapter_content_item,parent,false);
        return new ChapterContentViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(ChapterContentViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ChapterContent chapterContent = (ChapterContent) group.getItems().get(childIndex);
        holder.bind(chapterContent);
        chapterContentList.add(chapterContent);
//        chapterContentList.clear();
//



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String course_id = CourseEnrolledDetailsActivity.courseid;

                Log.d(TAG, "onClick: "+group.getItemCount());

                SharedPreferences sharedPreferences_t_m_c = mContext.getSharedPreferences("total_number_chapter",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences_t_m_c.edit();
                editor.putInt("total_number_chapter",total_number_chapter);
                editor.apply();

                Intent intent = new Intent(mContext, VideoPlayingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("course_id",course_id);
                intent.putExtra("chpt_id", flatPosition);
                intent.putExtra("child_chpt_id",childIndex);
                intent.putExtra("total_number_chapter",total_number_chapter);
                intent.putExtra("chapter_name", group.getTitle());
                intent.putExtra("chapterContentList",chapterContentList);
                mContext.startActivity(intent);

//                chapterContentList.clear();
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(ChapterNameViewHolder holder, int flatPosition, ExpandableGroup group) {
        final ChapterName chapterName = (ChapterName) group;
        holder.bind(chapterName);
        total_number_chapter = flatPosition;
        chapterNameArrayList.add(chapterName);

    }

    public interface OnItemClickListener{
        void onItemClick(int chpt_id, String chapter_name, ArrayList<ChapterContent> chapterContent);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }



}
