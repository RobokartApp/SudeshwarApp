package com.ark.robokart_robotics.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.R;

import java.util.List;

public class NotAdapter extends RecyclerView.Adapter<NotAdapter.MyViewHolder> {

    private List<NData> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, body,time;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title_not);
            body = view.findViewById(R.id.msg_not);
            time= view.findViewById(R.id.time);
        }
    }


    public NotAdapter(List<NData> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.not_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NData movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.body.setText(movie.getBody());
        holder.time.setText(movie.getTime());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}