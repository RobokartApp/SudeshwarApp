package com.ark.robokart_robotics.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

public class IntermediateCourseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "BlogAdapter";
    private List<CourseListModel> mcourseList;
    private ArrayList<String> selectedItemList;
    private final static int FADE_DURATION = 1000; //FADE_DURATION in milliseconds

    public IntermediateCourseListAdapter(List<CourseListModel> courseListModelList) {
        mcourseList = courseListModelList;
    }




    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        selectedItemList = new ArrayList<>();
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_row_video_item, parent, false));
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        if (mcourseList != null && mcourseList.size() > 0) {
            return mcourseList.size();
        } else {
            return 0;
        }
    }
    public class ViewHolder extends BaseViewHolder {

        ImageView ivVideo, overlay;
        TextView tvVideoName;
        TextView tvPeople,tvRating;
        LinearLayout recom_linear;


        public ViewHolder(View itemView) {
            super(itemView);

            ivVideo = itemView.findViewById(R.id.ivVideo);
            overlay = itemView.findViewById(R.id.overlay);
            tvVideoName = itemView.findViewById(R.id.tvVideoName);
            tvPeople = itemView.findViewById(R.id.tvPeople);
            tvRating = itemView.findViewById(R.id.tvRating);
            recom_linear = itemView.findViewById(R.id.recom_linear);



        }
        protected void clear() {
            tvVideoName.setText("");
            tvPeople.setText("");
            tvRating.setText("");
        }
        public void onBind(int position) {
            super.onBind(position);
            final CourseListModel courseListModel = mcourseList.get(position);

            if (courseListModel.getCourse_id() != null) {
                tvPeople.setText(courseListModel.getCourse_enrolled() + " " + "People");
            }
            if (courseListModel.getCourse_name() != null) {
                tvRating.setText(courseListModel.getCustomer_rating());
            }
            if (courseListModel.getCourse_enrolled() != null) {
                tvVideoName.setText(courseListModel.getCourse_name());
            }


            if(position %2 == 1)
            {
                overlay.setBackground(itemView.getResources().getDrawable(R.drawable.color3));
            }
            else
            {
                overlay.setBackground(itemView.getResources().getDrawable(R.drawable.color4));
            }

        }

        private void setScaleAnimation(View view) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }
}