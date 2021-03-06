package com.ark.robokart_robotics.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Model.Recommendations;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "BlogAdapter";
    private final List<Recommendations> mrecommendationsList;
    public static ArrayList<String> selectedItemList;




    public RecommendationAdapter(List<Recommendations> recommendationsList) {
        mrecommendationsList = recommendationsList;
    }




    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        selectedItemList = new ArrayList<>();
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recommendation, parent, false));
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        if (mrecommendationsList != null && mrecommendationsList.size() > 0) {
            return mrecommendationsList.size();
        } else {
            return 0;
        }
    }
    public class ViewHolder extends BaseViewHolder {

        TextView r_id;
        TextView r_name;
        LinearLayout recom_linear;
        public boolean isSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            r_id = itemView.findViewById(R.id.r_id);
            r_name = itemView.findViewById(R.id.r_name);
            recom_linear = itemView.findViewById(R.id.recom_linear);

        }
        protected void clear() {
            r_id.setText("");
            r_name.setText("");
        }
        public void onBind(int position) {
            super.onBind(position);
            final Recommendations recommendations = mrecommendationsList.get(position);

            if (recommendations.getR_id() != null) {
                r_id.setText(recommendations.getR_id());
            }
            if (recommendations.getR_name() != null) {
                r_name.setText(recommendations.getR_name());
            }


            recom_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String re_id =r_id.getText().toString();

                    if(selectedItemList.contains(re_id)){
                        selectedItemList.remove(re_id);
                        r_name.setBackground(v.getResources().getDrawable(R.drawable.tag_transparent_background));
                        r_name.setTextColor(v.getResources().getColor(R.color.black));
                        isSelected = false;

                    }else{
                        selectedItemList.add(re_id);
                        r_name.setBackground(v.getResources().getDrawable(R.drawable.tag_background));
                        r_name.setTextColor(v.getResources().getColor(R.color.white));
                        isSelected = true;

                    }


                }
            });


        }
    }
}