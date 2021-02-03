package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.Activities.Certificate.CertificateActivity;
import com.ark.robokart_robotics.Model.MyCertificateModel;
import com.ark.robokart_robotics.R;

import java.util.List;

public class MyCertificatesAdapter extends RecyclerView.Adapter<MyCertificatesAdapter.MyViewHolder> {

    private List<MyCertificateModel> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;
RelativeLayout cert_rel;


        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.course_name_tv);
            date = view.findViewById(R.id.certificate_date);
            cert_rel=view.findViewById(R.id.cert_relative);

        }
    }


    public MyCertificatesAdapter(List<MyCertificateModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_certificate_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyCertificateModel movie = moviesList.get(position);
        holder.name.setText(movie.getCourse_name());
        holder.date.setText(movie.getCourse_date());
        holder.cert_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CertificateActivity.class);
                intent.putExtra("cert_number",movie.getCert_number());
                intent.putExtra("cert_course",movie.getCourse_name());
                intent.putExtra("cert_date",movie.getCourse_date());
                intent.putExtra("result_id",movie.getResult_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}