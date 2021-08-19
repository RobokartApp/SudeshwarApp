package com.ark.robokart_robotics.Activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ark.robokart_robotics.Fragments.shop.FavListFragment;
import com.ark.robokart_robotics.R;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vimeo.networking.Vimeo;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemDetailActivity extends AppCompatActivity {
    RelativeLayout addToCart;
    ImageView addtofav;
    ImageView back_btn;
    ImageView cart_btn, wishlist;
    TextView description;
    ImageView minus_btn;
    TextView mrp;
    TextView name;
    ImageView plus_btn, share_btn, left_arrow, right_arrow;
    TextView price;
    TextView qty;
    TextView total_price;
    boolean favFlag = true;
    DBHelper dbHelper;
    String getname, getmrp, getdesc, getimgs, getprice, getItemid;
    ViewPager viewPager;
    ImageAdapter adapterView;
    long last_id = 0;
    TextView badge,countFavTv;
    int cartCount,countFav;
    String[] imgs;


    Handler handler = new Handler();
    Runnable runnable;
    int delay = 100;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_item_detail);
        this.dbHelper = new DBHelper(this);

        init();
        listener();

        cartCount = dbHelper.countCart();
        if (cartCount == 0) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + cartCount);
        }
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                extractedFavCount();
            }
        }, delay);

        Intent intent = getIntent();
        getname = intent.getStringExtra("name");
        getmrp = intent.getStringExtra("mrp");
        this.getprice = intent.getStringExtra("price");
        getdesc = intent.getStringExtra("desc");
        getimgs = intent.getStringExtra("images");
        getItemid = intent.getStringExtra("item_id");
        imgs = getimgs.split(",");

        this.name.setText(getname);
        this.mrp.setText("₹" + getmrp);
        TextView textView = this.mrp;
        textView.setPaintFlags(textView.getPaintFlags() | 16);
        this.price.setText("₹" + this.getprice);
        this.description.setText(getdesc);
        this.total_price.setText("₹ " + this.getprice);

        ArrayList<String> images = new ArrayList<String>(Arrays.asList(imgs));
        adapterView = new ImageAdapter(this, images);
        viewPager.setAdapter(adapterView);

        ArrayList<String> favList = dbHelper.getAllFav();
        if (favList.contains(getItemid)) {
            favFlag = false;
            addtofav.setImageResource(R.drawable.heart_filled);
        }

    }

    private void extractedFavCount() {
        countFav = dbHelper.countFev();
        if (countFav == 0) {
            countFavTv.setVisibility(View.GONE);
        } else {
            countFavTv.setVisibility(View.VISIBLE);
            countFavTv.setText("" + countFav);
        }
    }

    private void init() {
        wishlist = findViewById(R.id.wishlist_btn);
        countFavTv = findViewById(R.id.cart_wish_list_badge);
        badge = findViewById(R.id.cart_badge);
        left_arrow = findViewById(R.id.left_arrow);
        right_arrow = findViewById(R.id.right_arrow);
        viewPager = findViewById(R.id.images_pager);
        share_btn = findViewById(R.id.share_btn);
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
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left);
                transaction.replace(R.id.frame_container, new FavListFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        left_arrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() != 0)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true); //getItem(-1) for previous
                else
                    viewPager.setCurrentItem(adapterView.getCount());
            }
        });
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != adapterView.getCount() - 1)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                else
                    viewPager.setCurrentItem(0);

            }
        });

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
                if (dbHelper.isInCart(getname)) {
                    Toast.makeText(ItemDetailActivity.this, "Already in the cart!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.insertCart(getname, imgs[0], qty.getText().toString(), getmrp, getprice, getdesc);
                    Toast.makeText(ItemDetailActivity.this, "Item has been added to cart!", Toast.LENGTH_SHORT).show();
                    cartCount = dbHelper.countCart();
                    badge.setVisibility(View.VISIBLE);
                    badge.setText("" + cartCount);
                }

            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Robokart - Learn Robotics");
                    String shareMessage = "\nRobokart app रोबोटिक्स हो या कोडिंग सब कुछ इतने मजे से सीखते है कि एक बार में सब दिमाग में.. \n" +
                            "खुद ही देखलो.... मान जाओगे \uD83D\uDE07\n\n";
                    shareMessage = shareMessage + "https://robokart.com/app/share";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one to share the app"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        this.addtofav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (favFlag) {
                    favFlag = false;
                    addtofav.setImageResource(R.drawable.heart_filled);
                    last_id = dbHelper.insertFavSudesh(getname, imgs[0], getmrp, getprice, getItemid);
                } else {
                    favFlag = true;
                    addtofav.setImageResource(R.drawable.heart_empty);
//                    dbHelper.deleteFav(getname);
                    dbHelper.deleteFavInShopAdapter(getItemid);
                }
                extractedFavCount();
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

    @Override
    protected void onResume() {
        super.onResume();
        cartCount = dbHelper.countCart();
        if (cartCount == 0) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + cartCount);
        }
        extractedFavCount();
    }
}
