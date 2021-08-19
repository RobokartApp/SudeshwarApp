package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.data.DataHolder;
import com.vimeo.networking.Vimeo;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    Context context;
    private ArrayList<ShopItem> listdata;

    boolean favFlag=true;
    String getname,getmrp,getdesc,getimgs,getprice;
    String[] imgs;
    long last_id=0;
    DBHelper dbHelper;

    public ShopAdapter(Context context2, ArrayList<ShopItem> listdata2) {
        this.listdata = listdata2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_popular_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        //first
        final ShopItem shopItem = listdata.get(position);
        holder.name1.setText(shopItem.getName());
        String[] imgs_1=shopItem.getImages().split(",");

        String[] sArr_1=imgs_1[0].split("/");
        //"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
        String link_1="https://robokart.com/app_robo.png";
        if(sArr_1[2].equalsIgnoreCase("drive.google.com")) {
            String img_id=sArr_1[5];
            link_1 = "https://drive.google.com/uc?id=" + img_id;
        }
        else
            link_1=imgs_1[0];
        Glide.with(this.context).load(link_1).into(holder.item_img1);
        TextView textView_1 = holder.offer_price1;
        textView_1.setText("₹" + shopItem.getOffer_price());
        holder.category1.setText(shopItem.getCategory());
        holder.sub_cat1.setText(shopItem.getSubcategory());
        TextView textView2_1 = holder.mrp1;
        textView2_1.setText("₹" + shopItem.getMrp());
        holder.mrp1.setPaintFlags(holder.mrp1.getPaintFlags() | 16);
        dbHelper=new DBHelper(context);

        getname = shopItem.getName();
        getmrp = shopItem.getMrp();
        getprice =  shopItem.getOffer_price();
        getdesc =shopItem.getDescription();
        getimgs =  shopItem.getImages();
        imgs=getimgs.split(",");


        ArrayList<String> favList = dbHelper.getAllFav();
        if (favList.contains(String.valueOf(shopItem.getId()))) {
//            favFlag = false;
            holder.wishListCard.setImageResource(R.drawable.heart_filled);
        }

        //not working in db
        holder.wishListCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*if(favFlag) {
                    favFlag=false;
                    holder.wishListCard.setImageResource(R.drawable.heart_filled);
                    dbHelper.insertFavSudesh(getname, imgs[0], getmrp, getprice,shopItem.getId());
                }else{
                    favFlag=true;
                    holder.wishListCard.setImageResource(R.drawable.heart_empty);
                    dbHelper.deleteFavInShopAdapter(String.valueOf(shopItem.getId()));
                }*/
                Drawable drawable = holder.wishListCard.getDrawable();
                Drawable liked = context.getResources().getDrawable(R.drawable.heart_filled);
                Drawable disLiked = context.getResources().getDrawable(R.drawable.heart_empty);

                String imgStr=shopItem.getImages();
                String[] imgArr=imgStr.split(",");
                if (drawable.getConstantState().equals(disLiked.getConstantState())){
                    holder.wishListCard.setImageResource(R.drawable.heart_filled);
//                    dbHelper.insertFavSudesh(getname, imgs[0], getmrp, getprice,String.valueOf(shopItem.getId()));
                    last_id = dbHelper.insertFavSudesh(shopItem.getName(), imgArr[0], shopItem.getMrp(), shopItem.getOffer_price(), String.valueOf(shopItem.getId()));
                }else{
                    holder.wishListCard.setImageResource(R.drawable.heart_empty);
                    dbHelper.deleteFavInShopAdapter(String.valueOf(shopItem.getId()));
                }
            }
        });

        //not working in db
        holder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ShopAdapter.this.context, ItemDetailActivity.class);
                intent.putExtra("name", shopItem.getName());
                intent.putExtra("mrp", shopItem.getMrp());
                intent.putExtra("price", shopItem.getOffer_price());
                intent.putExtra("desc", shopItem.getDescription());
                intent.putExtra("images", shopItem.getImages());
                intent.putExtra("item_id", String.valueOf(shopItem.getId()));
                ShopAdapter.this.context.startActivity(intent);

            }
        });
/*
        //second
        final ShopItem shopItem_2 = this.listdata.get(position+1);
        holder.name2.setText(shopItem_2.getName());
        String[] imgs_2=shopItem_2.getImages().split(",");

        String[] sArr_2=imgs_2[0].split("/");
        //"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
        String link_2="https://robokart.com/app_robo.png";
        if(sArr_2[2].equalsIgnoreCase("drive.google.com")) {
            String img_id=sArr_2[5];
            link_2 = "https://drive.google.com/uc?id=" + img_id;
        }
        else
            link_2=imgs_2[0];
        Glide.with(this.context).load(link_2).into(holder.item_img2);
        TextView textView_2 = holder.offer_price2;
        textView_2.setText("₹" + shopItem_2.getOffer_price());
        if (shopItem_2.getCategory().isEmpty()){
            holder.category2.setVisibility(View.GONE);
        }else {
            holder.category2.setText(shopItem_2.getCategory());
        }
        if (shopItem_2.getCategory().isEmpty()){
            holder.sub_cat2.setVisibility(View.GONE);
        }else {
            holder.sub_cat2.setText(shopItem_2.getSubcategory());
        }
        TextView textView2_2 = holder.mrp2;
        textView2_2.setText("₹" + shopItem_2.getMrp());
        holder.mrp2.setPaintFlags(holder.mrp2.getPaintFlags() | 16);
        holder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ShopAdapter.this.context, ItemDetailActivity.class);
                intent.putExtra("name", shopItem_2.getName());
                intent.putExtra("mrp", shopItem_2.getMrp());
                intent.putExtra("price", shopItem_2.getOffer_price());
                intent.putExtra("desc", shopItem_2.getDescription());
                intent.putExtra("images", shopItem_2.getImages());
                ShopAdapter.this.context.startActivity(intent);
            }
        });*/
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public void updateList(ArrayList<ShopItem> list){
        listdata = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView relativeLayout1,relativeLayout2;
        public TextView category1,category2;
        public ImageView item_img1,item_img2,wishListCard;
        public TextView mrp1,mrp2;
        public TextView name1,name2;
        public TextView offer_price1,offer_price2;
        public TextView sub_cat1,sub_cat2;

        public ViewHolder(View itemView) {
            super(itemView);
            this.relativeLayout1 = (CardView) itemView.findViewById(R.id.shop_relative_1);
            this.wishListCard = (ImageView) itemView.findViewById(R.id.wish_list_card);
            this.item_img1 = (ImageView) itemView.findViewById(R.id.ivVideo_1);
            this.name1 = (TextView) itemView.findViewById(R.id.item_name_1);
            this.mrp1 = (TextView) itemView.findViewById(R.id.mrp_tv_1);
            this.offer_price1 = (TextView) itemView.findViewById(R.id.offer_price_tv_1);
            this.category1 = (TextView) itemView.findViewById(R.id.category_tv_1);
            this.sub_cat1 = (TextView) itemView.findViewById(R.id.subcat_tv_1);

//            this.relativeLayout2 = (CardView) itemView.findViewById(R.id.shop_relative_2);
//            this.item_img2 = (ImageView) itemView.findViewById(R.id.ivVideo_2);
//            this.name2 = (TextView) itemView.findViewById(R.id.item_name_2);
//            this.mrp2 = (TextView) itemView.findViewById(R.id.mrp_tv_2);
//            this.offer_price2 = (TextView) itemView.findViewById(R.id.offer_price_tv_2);
//            this.category2 = (TextView) itemView.findViewById(R.id.category_tv_2);
//            this.sub_cat2 = (TextView) itemView.findViewById(R.id.subcat_tv_2);
        }
    }
}
