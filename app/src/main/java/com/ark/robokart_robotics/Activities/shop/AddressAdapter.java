package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    ArrayList<String> address;
    Context context;
    ArrayList<String> ids;
    /* access modifiers changed from: private */
    public final OnItemClickListener listener;
    ArrayList<String> names;
    ArrayList<String> phone;

    public interface OnItemClickListener {
        void onItemClick(int i, int i2);
    }

    public AddressAdapter(Context context2, ArrayList<String> ids2, ArrayList<String> names2, ArrayList<String> address2, ArrayList<String> phone2, OnItemClickListener listener2) {
        this.names = names2;
        this.ids = ids2;
        this.address = address2;
        this.phone = phone2;
        this.listener = listener2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_address, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int id = Integer.parseInt(this.ids.get(position));
        holder.usr_name.setText(this.names.get(position));
        holder.adr_tv.setText(this.address.get(position));
        holder.mob_tv.setText(this.phone.get(position));
        holder.select_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddressAdapter.this.listener.onItemClick(position, id);
            }
        });
    }

    public int getItemCount() {
        return this.names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView adr_tv;
        public TextView mob_tv;
        public TextView select_btn;
        public TextView usr_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.usr_name = (TextView) itemView.findViewById(R.id.user_name);
            this.adr_tv = (TextView) itemView.findViewById(R.id.address_tv);
            this.mob_tv = (TextView) itemView.findViewById(R.id.address_phone);
            this.select_btn = (TextView) itemView.findViewById(R.id.select_btn);
        }
    }
}
