package com.ark.robokart_robotics.Activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.R;

public class CategoryItemsActivity extends AppCompatActivity {
    ImageView back_btn;
    ImageView cart_btn;
    TextView cat_title;
    RecyclerView recyclerView;
    ShopItem[] shopItems;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_category_items);
        this.recyclerView = (RecyclerView) findViewById(R.id.popular_recycler);
        this.cat_title = (TextView) findViewById(R.id.category_name);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.cart_btn = (ImageView) findViewById(R.id.cart_btn);
        this.cat_title.setText(getIntent().getStringExtra("name"));
        ShopItem[] shopItemArr = {new ShopItem(1, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "100 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(2, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "200 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(3, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "300 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(4, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "500 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"), new ShopItem(5, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "400 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag")};
        this.shopItems = shopItemArr;
        this.recyclerView.setAdapter(new ShopAdapter(this, shopItemArr));
        this.cart_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CategoryItemsActivity.this.startActivity(new Intent(CategoryItemsActivity.this, CartActivity.class));
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CategoryItemsActivity.this.onBackPressed();
            }
        });
    }
}
