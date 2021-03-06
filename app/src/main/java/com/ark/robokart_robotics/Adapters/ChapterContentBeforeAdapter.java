package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Model.Class_chapters;
import com.ark.robokart_robotics.R;

import java.util.List;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class ChapterContentBeforeAdapter extends RecyclerView.Adapter<ChapterContentBeforeAdapter.MyViewHolder> {

    private final List<Class_chapters.Course_List> course;
    private final Context mContext;

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

    public ChapterContentBeforeAdapter(Context context, List<Class_chapters.Course_List> course) {
        mContext = context;
        this.course = course;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chapter_content_before_buy_item, parent,false);
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
                if (!image.getVideo_time().equals("null"))
                    holder.tv_mins.setText("Video - " + image.getVideo_time() + " mins");
                else
                    holder.tv_mins.setText("Video");
            }
            if(assign.length() > 0) {
                holder.tv_mins.setText("Assignment Test");
            }
            if(quiz.length() > 0) {
                holder.tv_mins.setText("Quiz test");
            }



        }catch (Exception ex){

        }
    }

    @Override
    public int getItemCount() {
        return course.size();
    }

}
