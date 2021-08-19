package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    Context context;
    /* access modifiers changed from: private */
    public ArrayList<OrderDetailItem> listdata;

    public OrderDetailAdapter(Context context2, ArrayList<OrderDetailItem> listdata2) {
        this.listdata = listdata2;
        this.context = context2;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_detail_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderDetailItem shopItem = this.listdata.get(position);
        holder.name.setText(this.listdata.get(position).getName());
        String[] sArr=shopItem.getImages().split("/");
        //"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
        String link="https://robokart.com/app_robo.png";
        if(sArr[2].equalsIgnoreCase("drive.google.com")) {
            String img_id=sArr[5];
            link = "https://drive.google.com/uc?id=" + img_id;
        }
        else
            link=shopItem.getImages();
        Glide.with(this.context).load(link).into(holder.item_img);
        holder.qty.setText("X "+shopItem.getQty());
        TextView textView = holder.offer_price;
        textView.setText("₹" + shopItem.getOffer_price());
        this.listdata.size();
        TextView textView2 = holder.mrp;
        textView2.setText("₹" + shopItem.getMrp());
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | 16);

    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView item_img;
        public TextView mrp,qty;
        public TextView name;
        public TextView offer_price;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_img = (ImageView) itemView.findViewById(R.id.item_img);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.mrp = (TextView) itemView.findViewById(R.id.item_mrp);
            this.offer_price = (TextView) itemView.findViewById(R.id.item_price);
            qty=itemView.findViewById(R.id.item_qty);

        }
    }
}
