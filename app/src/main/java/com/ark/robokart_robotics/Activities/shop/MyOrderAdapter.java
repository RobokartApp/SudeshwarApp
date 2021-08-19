package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    /* access modifiers changed from: private */
    public ArrayList<OrderItem> listdata;

    public MyOrderAdapter(Context context2, ArrayList<OrderItem> listdata2) {
        this.listdata = listdata2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderItem shopItem = this.listdata.get(position);
        holder.name.setText(this.listdata.get(position).getName());

        String[] sArr=shopItem.getImg().split("/");
        //"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
        String link="https://robokart.com/app_robo.png";
        if(sArr[2].equalsIgnoreCase("drive.google.com")) {
            String img_id=sArr[5];
            link = "https://drive.google.com/uc?id=" + img_id;
        }
        else
            link=shopItem.getImg();
        Glide.with(this.context).load(link).into(holder.item_img);
        holder.delivery_txt.setText(listdata.get(position).getDelivery());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Order Detail", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,OrderDetailActivity.class);
                intent.putExtra("order_id",shopItem.getOrder_id());
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_img;
        public TextView delivery_txt;
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item_img = (ImageView) itemView.findViewById(R.id.item_img);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.delivery_txt = (TextView) itemView.findViewById(R.id.delivery_text);
        }
    }
}
