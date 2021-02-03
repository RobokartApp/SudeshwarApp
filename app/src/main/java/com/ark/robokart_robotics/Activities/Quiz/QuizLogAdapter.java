package com.ark.robokart_robotics.Activities.Quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

public class QuizLogAdapter extends RecyclerView.Adapter<QuizLogAdapter.ViewHolder>{
    private ArrayList<QuizLog> listdata;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // RecyclerView recyclerView;
    public QuizLogAdapter(ArrayList<QuizLog> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_dailyquiz, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(listdata.get(position).getIsGiven()==1){
            holder.status_img.setImageResource(R.drawable.test_complete);
        }else
            holder.status_img.setImageResource(R.drawable.test_pending);

        holder.quiz_id.setText(listdata.get(position).getQuiz_id());
        holder.quiz_date.setText(listdata.get(position).getQuiz_date());
        holder.ques_count.setText("Total Questions : "+listdata.get(position).getCount());
        holder.result.setText(listdata.get(position).getResult());

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView status_img;
        public TextView quiz_id,quiz_date,ques_count,result;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.status_img = itemView.findViewById(R.id.iv_status);
            this.quiz_id = itemView.findViewById(R.id.tv_quiz_id);
            quiz_date=itemView.findViewById(R.id.tv_date);
            ques_count=itemView.findViewById(R.id.tv_count_ques);
            result=itemView.findViewById(R.id.tv_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}