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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public static double sumMrp;
    public static double sumPrice;
    Context context;
    /* access modifiers changed from: private */
    public ArrayList<CartItem> listdata;
    /* access modifiers changed from: private */
    public final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i, ArrayList<CartItem> arrayList);

        void onSelect(int i, ArrayList<CartItem> arrayList, AdapterView<?> adapterView, int i2);
    }

    public CartAdapter(Context context2, ArrayList<CartItem> listdata2, OnItemClickListener listener2) {
        this.listdata = listdata2;
        this.context = context2;
        this.listener = listener2;
        sumMrp = 0.0d;
        sumPrice = 0.0d;
        for (int i = 0; i < listdata2.size(); i++) {
            sumMrp += (Double.parseDouble(listdata2.get(i).getMrp())*listdata2.get(i).getQty());
            sumPrice += (Double.parseDouble(listdata2.get(i).getOffer_price())*listdata2.get(i).getQty());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        CartItem shopItem = this.listdata.get(position);
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
        TextView textView = holder.offer_price;
        textView.setText("₹" + shopItem.getOffer_price());
        this.listdata.size();
        TextView textView2 = holder.mrp;
        textView2.setText("₹" + shopItem.getMrp());
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | 16);
        List<String> categories = new ArrayList<>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        holder.spinner.setAdapter(dataAdapter);
        holder.spinner.setSelection(shopItem.getQty() - 1);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int positionIn, long id) {
                CartAdapter.this.listener.onSelect(positionIn, CartAdapter.this.listdata, parent, position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CartAdapter.this.listener.onItemClick(position, CartAdapter.this.listdata);
            }
        });
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public ImageView delete_btn;
        public ImageView item_img;
        public TextView mrp;
        public TextView name;
        public TextView offer_price;
        public RelativeLayout relativeLayout;
        Spinner spinner;
        public TextView sub_cat;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_img = (ImageView) itemView.findViewById(R.id.item_img);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.mrp = (TextView) itemView.findViewById(R.id.item_mrp);
            this.offer_price = (TextView) itemView.findViewById(R.id.item_price);
            this.delete_btn = (ImageView) itemView.findViewById(R.id.delete_ic);
            this.spinner = (Spinner) itemView.findViewById(R.id.spinner);
        }
    }
}
