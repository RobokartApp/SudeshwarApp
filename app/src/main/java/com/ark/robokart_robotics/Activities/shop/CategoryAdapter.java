package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    private ArrayList<CatItem> listdata;

    public CategoryAdapter(Context context2, ArrayList<CatItem>  listdata2) {
        this.listdata = listdata2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final CatItem catItem = this.listdata.get(position);
        holder.cat_name.setText(this.listdata.get(position).getCat_name());

        Glide.with(this.context).load(catItem.cat_img).into(holder.cat_img);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CategoryAdapter.this.context, CategoryItemsActivity.class);
                intent.putExtra("name", catItem.getCat_name());
                CategoryAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView cat_img;
        public TextView cat_name;
        public LinearLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cat_img = (ImageView) itemView.findViewById(R.id.cat_img);
            this.cat_name = (TextView) itemView.findViewById(R.id.tv_category_name);
            this.relativeLayout = (LinearLayout) itemView.findViewById(R.id.category_relative);
        }
    }
}
