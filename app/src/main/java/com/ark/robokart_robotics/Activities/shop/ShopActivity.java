package com.ark.robokart_robotics.Activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    ImageView back_btn;
    ImageView cart_btn;
    CatItem[] catItems;
    RecyclerView category_recycler;
    DBHelper dbHelper;
    RecyclerView popular_recycler;
    EditText search;
    ShopAdapter shopAdapter;
    ShopItem[] shopItems;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_shop);
        init();
        listener();
        DBHelper dBHelper = new DBHelper(this);
        this.dbHelper = dBHelper;
        ArrayList<String> arrayList = dBHelper.getAllCart();
        Log.e("cart records", "" + arrayList);
        ShopItem[] shopItemArr = {new ShopItem(1, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "100 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(2, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "200 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(3, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "300 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(4, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "500 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(5, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "400 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag")};
        this.shopItems = shopItemArr;
        ShopAdapter shopAdapter2 = new ShopAdapter(this, shopItemArr);
        this.shopAdapter = shopAdapter2;
        this.popular_recycler.setAdapter(shopAdapter2);
        CatItem[] catItemArr = {new CatItem("https://robokart.com/assets/img/cat2-min.png", "Drone"), new CatItem("https://robokart.com/assets/img/cat1-min.png", "Robotics"), new CatItem("https://robokart.com/assets/img/cat3-min.png", "3D Printing"), new CatItem("https://robokart.com/assets/img/cat4-min.png", "AI")};
        this.catItems = catItemArr;
        this.category_recycler.setAdapter(new CategoryAdapter(this, catItemArr));
    }

    private void listener() {
        this.cart_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShopActivity.this.startActivity(new Intent(ShopActivity.this, CartActivity.class));
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShopActivity.this.onBackPressed();
            }
        });
    }

    private void init() {
        this.search = (EditText) findViewById(R.id.search_bar);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.cart_btn = (ImageView) findViewById(R.id.cart_btn);
        this.popular_recycler = (RecyclerView) findViewById(R.id.popular_recycler);
        this.category_recycler = (RecyclerView) findViewById(R.id.category_recycler);
    }
}
