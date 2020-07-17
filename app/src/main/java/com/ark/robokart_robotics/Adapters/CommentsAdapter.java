package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Model.CommentModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CommentsAdapter extends ListAdapter<CommentModel,CommentsAdapter.CommentsHolder> {

    private List<CommentModel> mcourseList;
    private Context mContext;

    public CommentsAdapter(Context context, List<CommentModel> courseListModelList) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mcourseList = courseListModelList;
    }

    private static final DiffUtil.ItemCallback<CommentModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<CommentModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommentModel oldItem, @NonNull CommentModel newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentModel oldItem, @NonNull CommentModel newItem) {
            return oldItem.getComment().equals(newItem.getComment());
        }
    };

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment_item,parent,false);
        return new CommentsHolder(itemView);
    }


    public class CommentsHolder extends RecyclerView.ViewHolder{
        ImageView user_profile_image, overlay;
        TextView user_name;
        TextView comment;
        RelativeLayout video_relative;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);

            user_profile_image = itemView.findViewById(R.id.user_profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            comment = itemView.findViewById(R.id.comment);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
        CommentModel commentModel = mcourseList.get(position);

        holder.user_name.setText(commentModel.getCustomer_name());
        holder.comment.setText(commentModel.getComment());

        Glide.with(mContext).load(commentModel.getCustomer_image()).into(holder.user_profile_image);
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





}
