package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    Context context;
    ArrayList<String> coupon;
    ArrayList<String> details;

    public CouponAdapter(Context context2, ArrayList<String> coupon2, ArrayList<String> details2) {
        this.details = details2;
        this.coupon = coupon2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_coupon, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final String name = this.coupon.get(position);
        holder.cpn_name.setText(name);
        holder.cpn_details.setText(this.details.get(position));
        holder.apply_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Context context = view.getContext();
                Toast.makeText(context, "click on item: " + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return this.coupon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView apply_btn;
        public TextView cpn_details;
        public TextView cpn_name;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cpn_details = (TextView) itemView.findViewById(R.id.coupon_details);
            this.cpn_name = (TextView) itemView.findViewById(R.id.item_name);
            this.apply_btn = (TextView) itemView.findViewById(R.id.apply_btn);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative);
        }
    }
}
