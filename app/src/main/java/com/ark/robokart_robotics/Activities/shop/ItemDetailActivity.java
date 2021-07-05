package com.ark.robokart_robotics.Activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ark.robokart_robotics.R;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vimeo.networking.Vimeo;

public class ItemDetailActivity extends AppCompatActivity {
    RelativeLayout addToCart;
    ImageView addtofav;
    ImageView back_btn;
    ImageView cart_btn;
    TextView description;
    String getprice;
    ImageView minus_btn;
    TextView mrp;
    TextView name;
    ImageView plus_btn,share_btn;
    TextView price;
    TextView qty;
    TextView total_price;
    boolean favFlag=true;
    DBHelper dbHelper;
    String getname,getmrp;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_item_detail);
        this.dbHelper = new DBHelper(this);

        init();
        listener();
        Intent intent = getIntent();
         getname = intent.getStringExtra("name");
         getmrp = intent.getStringExtra("mrp");
        this.getprice = intent.getStringExtra(FirebaseAnalytics.Param.PRICE);
        String getdesc = intent.getStringExtra(Vimeo.SORT_DIRECTION_DESCENDING);
        this.name.setText(getname);
        this.mrp.setText("₹" + getmrp);
        TextView textView = this.mrp;
        textView.setPaintFlags(textView.getPaintFlags() | 16);
        this.price.setText("₹" + this.getprice);
        this.description.setText(getdesc);
        this.total_price.setText("₹ " + this.getprice);
    }

    private void init() {
        share_btn=findViewById(R.id.share_btn);
        this.cart_btn = (ImageView) findViewById(R.id.cart_btn);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.plus_btn = (ImageView) findViewById(R.id.plus_btn);
        this.minus_btn = (ImageView) findViewById(R.id.minus_btn);
        this.addToCart = (RelativeLayout) findViewById(R.id.addtocart_rl);
        this.addtofav = (ImageView) findViewById(R.id.addtofav_iv);
        this.name = (TextView) findViewById(R.id.item_name);
        this.mrp = (TextView) findViewById(R.id.item_mrp);
        this.price = (TextView) findViewById(R.id.item_price);
        this.qty = (TextView) findViewById(R.id.item_qty);
        this.total_price = (TextView) findViewById(R.id.total_price);
        this.description = (TextView) findViewById(R.id.item_description);
    }

    private void listener() {
        this.cart_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItemDetailActivity.this.startActivity(new Intent(ItemDetailActivity.this, CartActivity.class));
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onBackPressed();
            }
        });
        this.addToCart.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dbHelper.insertCart(getname,"https://robokart.com/app/app_robo.png",qty.getText().toString(),getmrp,getprice,"1");
                Toast.makeText(ItemDetailActivity.this, "Item has been added to cart!", Toast.LENGTH_SHORT).show();
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.addtofav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(favFlag) {
                    favFlag=false;
                    addtofav.setImageResource(R.drawable.heart_filled);
                }else{
                    favFlag=true;
                    addtofav.setImageResource(R.drawable.heart_empty);
                }
                //Toast.makeText(ItemDetailActivity.this, "Added to Favorite!", Toast.LENGTH_SHORT).show();
            }
        });
        this.plus_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int q = Integer.parseInt(ItemDetailActivity.this.qty.getText().toString()) + 1;
                ItemDetailActivity.this.qty.setText("" + q);
                ItemDetailActivity.this.total_price.setText("₹" + (Integer.parseInt(ItemDetailActivity.this.getprice) * q));
            }
        });
        this.minus_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!ItemDetailActivity.this.qty.getText().equals(IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE)) {
                    int q = Integer.parseInt(ItemDetailActivity.this.qty.getText().toString()) - 1;
                    ItemDetailActivity.this.qty.setText("" + q);
                    ItemDetailActivity.this.total_price.setText("₹ " + (Integer.parseInt(ItemDetailActivity.this.getprice) * q));
                }
            }
        });
    }

}
