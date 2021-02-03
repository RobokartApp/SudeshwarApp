package com.ark.robokart_robotics.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Model.StandardModel;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

public class StandardAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "BlogAdapter";
    private final List<StandardModel> mStandardList;
    public static ArrayList<String> stdselectedList;


    public StandardAdapter(List<StandardModel> recommendationsList) {
        mStandardList = recommendationsList;
    }




    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        stdselectedList = new ArrayList<>();
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_standard, parent, false));
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        if (mStandardList != null && mStandardList.size() > 0) {
            return mStandardList.size();
        } else {
            return 0;
        }
    }
    public class ViewHolder extends BaseViewHolder {

        TextView s_id;
        TextView s_name;
        LinearLayout recom_linear;
        public boolean isSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            s_id = itemView.findViewById(R.id.r_id);
            s_name = itemView.findViewById(R.id.r_name);
            recom_linear = itemView.findViewById(R.id.recom_linear);

        }
        protected void clear() {
            s_id.setText("");
            s_name.setText("");
        }
        public void onBind(int position) {
            super.onBind(position);
            final StandardModel recommendations = mStandardList.get(position);

            if (recommendations.getS_id() != null) {
                s_id.setText(recommendations.getS_id());
            }
            if (recommendations.getS_name() != null) {
                s_name.setText(recommendations.getS_name());
            }


            recom_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String re_id =s_id.getText().toString();

                    if(stdselectedList.contains(re_id)){
                        stdselectedList.remove(re_id);
                        s_name.setBackground(v.getResources().getDrawable(R.drawable.tag_transparent_background));
                        s_name.setTextColor(v.getResources().getColor(R.color.black));
                        isSelected = false;
                    }else{
                        stdselectedList.add(re_id);
                        s_name.setBackground(v.getResources().getDrawable(R.drawable.tag_background));
                        s_name.setTextColor(v.getResources().getColor(R.color.white));
                        isSelected = true;
                    }

                }
            });


        }
    }
}
