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
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.vimeo.networking.Vimeo;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    Context context;
    /* access modifiers changed from: private */
    public ArrayList<CartItem> listdata;
    /* access modifiers changed from: private */
    public final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i, ArrayList<CartItem> arrayList);
    }

    public FavAdapter(Context context2, ArrayList<CartItem> listdata2, OnItemClickListener listener2) {
        this.listdata = listdata2;
        this.context = context2;
        this.listener = listener2;

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fav_item, parent, false));
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

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FavAdapter.this.listener.onItemClick(position, FavAdapter.this.listdata);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavAdapter.this.context, ItemDetailActivity.class);
                intent.putExtra("name", shopItem.getName());
                intent.putExtra(DBHelper.CONTACTS_COLUMN_CITY, shopItem.getMrp());
                intent.putExtra("price", shopItem.getOffer_price());
                intent.putExtra(Vimeo.SORT_DIRECTION_DESCENDING, shopItem.getDescription());
                intent.putExtra("images", shopItem.getImages());
                context.startActivity(intent);
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
        public TextView sub_cat;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_img = (ImageView) itemView.findViewById(R.id.item_img);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.mrp = (TextView) itemView.findViewById(R.id.item_mrp);
            this.offer_price = (TextView) itemView.findViewById(R.id.item_price);
            this.delete_btn = (ImageView) itemView.findViewById(R.id.delete_ic);
        }
    }
}
