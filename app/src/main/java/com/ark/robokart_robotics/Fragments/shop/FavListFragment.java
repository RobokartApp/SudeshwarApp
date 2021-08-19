package com.ark.robokart_robotics.Fragments.shop;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.robokart_robotics.Activities.shop.CartActivity;
import com.ark.robokart_robotics.Activities.shop.CartAdapter;
import com.ark.robokart_robotics.Activities.shop.CartItem;
import com.ark.robokart_robotics.Activities.shop.DBHelper;
import com.ark.robokart_robotics.Activities.shop.FavAdapter;
import com.ark.robokart_robotics.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavListFragment extends Fragment {

    Fragment fragment;
    public FavListFragment() {
        // Required empty public constructor
        fragment=this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_list, container, false);
    }

    public ArrayList<CartItem> listdata;
    ArrayList<CartItem> favItems;
    DBHelper dbHelper;
    FavAdapter favAdapter;
    RecyclerView favRecycler;
    TextView empty_list;
    ImageView back_bnt;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBHelper dBHelper = new DBHelper(getContext());
        this.dbHelper = dBHelper;
        Cursor res = dBHelper.getFavData();

        favRecycler=view.findViewById(R.id.fav_item_recycle);
        empty_list=view.findViewById(R.id.empty_wishlist);
        back_bnt=view.findViewById(R.id.back_btn);

        if (res.getCount() != 0) {
            empty_list.setVisibility(View.GONE);
            favRecycler.setVisibility(View.VISIBLE);
            favItems = new ArrayList<>();
            while (res.moveToNext()) {
                Log.e("cart val", "" + res.getInt(0));
                this.favItems.add(
                        new CartItem(
                                res.getInt(0),//id
                                res.getString(2),//image
                                res.getString(1),//name
                                res.getString(4),//price
                                res.getString(3),//mrp
                                1,//qty
                                res.getString(5))//description
                );
            }

            FavAdapter.OnItemClickListener r1 = new FavAdapter.OnItemClickListener() {

                public void onItemClick(final int positon, final ArrayList<CartItem> list) {
                    //Log.e("before delete vals", "mrp: " + CartActivity.this.sum1 + "\nprice: " + CartActivity.this.sum2);
                    new AlertDialog.Builder(getContext()).setMessage((CharSequence) "Are you sure to delete?").setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            dbHelper.deleteFav(list.get(positon).getName());
                            list.remove(positon);
                            favAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).show();
                }
            };

            favAdapter = new FavAdapter(getContext(), favItems, r1);
            favRecycler.setAdapter(favAdapter);
        }else{
            empty_list.setVisibility(View.VISIBLE);
            favRecycler.setVisibility(View.GONE);
        }

        back_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FavListFragment.this).commit();
            }
        });
    }
}