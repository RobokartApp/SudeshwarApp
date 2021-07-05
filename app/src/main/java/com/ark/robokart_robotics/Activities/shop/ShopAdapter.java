package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.vimeo.networking.Vimeo;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    Context context;
    private ShopItem[] listdata;

    public ShopAdapter(Context context2, ShopItem[] listdata2) {
        this.listdata = listdata2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_popular_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShopItem shopItem = this.listdata[position];
        holder.name.setText(this.listdata[position].getName());
        Glide.with(this.context).load(shopItem.getImages()).into(holder.item_img);
        TextView textView = holder.offer_price;
        textView.setText("₹" + shopItem.getOffer_price());
        holder.category.setText(shopItem.getCategory());
        holder.sub_cat.setText(shopItem.getSubcategory());
        TextView textView2 = holder.mrp;
        textView2.setText("₹" + shopItem.getMrp());
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | 16);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ShopAdapter.this.context, ItemDetailActivity.class);
                intent.putExtra("name", shopItem.getName());
                intent.putExtra(DBHelper.CONTACTS_COLUMN_CITY, shopItem.getMrp());
                intent.putExtra("price", shopItem.getOffer_price());
                intent.putExtra(Vimeo.SORT_DIRECTION_DESCENDING, shopItem.getDescription());
                intent.putExtra("images", shopItem.getImages());
                ShopAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public ImageView item_img;
        public TextView mrp;
        public TextView name;
        public TextView offer_price;
        public RelativeLayout relativeLayout;
        public TextView sub_cat;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_img = (ImageView) itemView.findViewById(R.id.ivVideo);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.mrp = (TextView) itemView.findViewById(R.id.mrp_tv);
            this.offer_price = (TextView) itemView.findViewById(R.id.offer_price_tv);
            this.category = (TextView) itemView.findViewById(R.id.category_tv);
            this.sub_cat = (TextView) itemView.findViewById(R.id.subcat_tv);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.shop_relative);
        }
    }
}
