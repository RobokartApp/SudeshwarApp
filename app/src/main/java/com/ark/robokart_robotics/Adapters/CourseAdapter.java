package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.CourseEnrolled.CourseEnrolledDetailsActivity;
import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingActivity;
import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private final List<Class_chapters.Course_List> course;
    private final Context mContext;
    public static int quiz_counter=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_course;
        private final TextView tv_mins;
        private final LinearLayout chapter_content_linear;

        public MyViewHolder(View view) {
            super(view);
            tv_course = view.findViewById(R.id.chapter_content);
            tv_mins = view.findViewById(R.id.video_mins);
            chapter_content_linear = view.findViewById(R.id.chapter_content_linear);
        }
    }

    public CourseAdapter(Context context, List<Class_chapters.Course_List> course) {
        mContext = context;
        this.course = course;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chapter_content_item, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try
        {
            final Class_chapters.Course_List image = course.get(position);

            String quiz = image.getQuiz_id();

            String assign = image.getAssignment_url();

            holder.tv_course.setText(image.getChapter_content());

            if(quiz.equals("") || assign.equals("")){
                holder.tv_mins.setText("Video - " + image.getVideo_time() + " mins");
            }
            if(assign.length() > 0) {
                holder.tv_mins.setText("Assignment Test");
            }
            if(quiz.length() > 0) {
                holder.tv_mins.setText("Quiz test");
            }


            holder.chapter_content_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(), course.size()+"",Toast.LENGTH_SHORT).show();

                    int num = image.getNum();

                    SharedPreferences chapt_completed = mContext.getSharedPreferences("courseaccess",Context.MODE_PRIVATE);

                    int chapter_completed = Integer.valueOf(chapt_completed.getString("chapter_completed",""));

                    if(num <=chapter_completed) {

                        //Toast.makeText(mContext, num + "", Toast.LENGTH_SHORT).show();
quiz_counter=num+1;
                        String course_id = CourseEnrolledDetailsActivity.courseid;

                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("chapter", Context.MODE_PRIVATE);

                        String chpt_id = sharedPreferences.getString("chpt_id", "");

                        String chapter_name = sharedPreferences.getString("chapter_name", "");

                        String total_number_chapter = sharedPreferences.getString("total_number_chapter", "");

                        Bundle bundle = new Bundle();
                        bundle.putString("course_id", course_id);
                        bundle.putString("chpt_id", chpt_id);
                        bundle.putString("child_chpt_id", String.valueOf(position + 1));
                        bundle.putString("total_number_chapter", total_number_chapter);
                        bundle.putString("chapter_name", chapter_name);
                        bundle.putParcelableArrayList("courses", (ArrayList<Class_chapters.Course_List>) course);

                        Intent intent = new Intent(mContext, VideoPlayingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);

                    }
                }
            });

        }catch (Exception ex){

        }
    }

    @Override
    public int getItemCount() {
        return course.size();
    }

}
