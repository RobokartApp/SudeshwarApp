package com.ark.robokart_robotics.Activities.shop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Fragments.shop.FavListFragment;
import com.ark.robokart_robotics.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopActivity extends AppCompatActivity {

    ImageView back_btn;
    ImageView cart_btn, wishlist;
    RecyclerView category_recycler;
    DBHelper dbHelper;
    RecyclerView popular_recycler;
    EditText search;
    ShimmerFrameLayout shimmerFrameLayout;
    ShopAdapter shopAdapter;
    CategoryAdapter categoryAdapter;
    TextView badge,wishListBadge;
    int cartCount,fevCount;
    ArrayList<ShopItem> shopItems;
    ArrayList<CatItem> catsItems;
    String TAG = "Shop Act";
    ProgressBar progressBar;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 100;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_shop);
        init();

        progressBar.setVisibility(View.VISIBLE);
        popular_recycler.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        listener();
        DBHelper dBHelper = new DBHelper(this);
        this.dbHelper = dBHelper;

//        progressBar.setVisibility(View.VISIBLE);
        //dbHelper.getAllFav();
        //Toast.makeText(this, "Cart:"+dbHelper.countCart(), Toast.LENGTH_SHORT).show();
        cartCount = dbHelper.countCart();
        if (cartCount == 0) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + cartCount);
        }
        //ArrayList<String> arrayList = dBHelper.getAllCart();
        //Log.e("cart records", "" + arrayList);
        countLoopMethod();

        getAllCatagory();

        getAllShop();


//        CatItem[] catItemArr = {new CatItem("https://robokart.com/assets/img/cat2-min.png", "Drone"),
//                new CatItem("https://robokart.com/assets/img/cat1-min.png", "Robotics"),
//                new CatItem("https://robokart.com/assets/img/cat3-min.png", "3D Printing"),
//                new CatItem("https://robokart.com/assets/img/cat4-min.png", "AI")};
//        this.catItems = catItemArr;
//        this.category_recycler.setAdapter(new CategoryAdapter(this, catItemArr));
    }

    private void countLoopMethod() {

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
//                fevCountMethod();

                extractedFavCount();
            }
        }, delay);

    }

    private void fevCountMethod() {
        //count display for fev
        Cursor res = dbHelper.getFavData();
        fevCount = res.getCount();
        if (fevCount == 0) {
            wishListBadge.setVisibility(View.GONE);
        } else {
            wishListBadge.setVisibility(View.VISIBLE);
            wishListBadge.setText("" + fevCount);
        }
    }


    private void extractedFavCount() {
        fevCount = dbHelper.countFev();
        if (fevCount == 0) {
            wishListBadge.setVisibility(View.GONE);
        } else {
            wishListBadge.setVisibility(View.VISIBLE);
            wishListBadge.setText("" + fevCount);
        }
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
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

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

    void filter(String text) {
        ArrayList<ShopItem> temp = new ArrayList<>();
        for (ShopItem d : shopItems) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        shopAdapter.updateList(temp);
    }

    private void init() {
         shimmerFrameLayout=findViewById(R.id.shop_activity_shimmer);
         wishlist = findViewById(R.id.wishlist_btn);
         wishListBadge = findViewById(R.id.cart_wish_list_badge);
         progressBar = findViewById(R.id.progressBar);
         badge = findViewById(R.id.cart_badge);
        this.search = (EditText) findViewById(R.id.search_bar);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.cart_btn = (ImageView) findViewById(R.id.cart_btn);
        this.popular_recycler = (RecyclerView) findViewById(R.id.popular_recycler);
        this.category_recycler = (RecyclerView) findViewById(R.id.category_recycler);
    }

    public void getAllCatagory() {

//        CatItem[] catItemArr = {new CatItem("https://robokart.com/assets/img/cat2-min.png", "Drone"),
//                new CatItem("https://robokart.com/assets/img/cat1-min.png", "Robotics"),
//                new CatItem("https://robokart.com/assets/img/cat3-min.png", "3D Printing"),
//                new CatItem("https://robokart.com/assets/img/cat4-min.png", "AI")};
//        this.catItems = catItemArr;
//        this.category_recycler.setAdapter(new CategoryAdapter(this, catItemArr));

        catsItems = new ArrayList<>();
        final CatItem[][] catItemArr = new CatItem[1][1];
        RequestQueue requestQueue = Volley.newRequestQueue(ShopActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://robokart.com/Api/app_shop_category_test/app_shop_category_test.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("cats");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.e("respo MyOrders", response);
                if (status == 1) {
                    try {
                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject json = courses.getJSONObject(i);

                            catsItems.add(new CatItem(
                                    json.getString("image"),//1
                                    json.getString("name")//2
                            ));
                        }
                        /*catItemArr[0] = new CatItem[catsItems.size()];// ArrayList to String Array conversion
                        for(int j =0;j<catsItems.size();j++){
                            catItemArr[0][j] = catsItems.get(j);
                        }*/
                    } catch (Exception e) {
                        //Toast.makeText(ShopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }

//                    progressBar.setVisibility(View.GONE);
//                    categoryAdapter = new CategoryAdapter(ShopActivity.this, catsItems);
//                    category_recycler.setAdapter(shopAdapter);
                } else if (status == 0) {
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "fetchLocationListing: " + e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Volley error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);
//                    shopAdapter = new ShopAdapter(ShopActivity.this, shopItems);

                    categoryAdapter = new CategoryAdapter(ShopActivity.this, catsItems);
                    category_recycler.setAdapter(categoryAdapter);
                //Toast.makeText(ShopActivity.this, "items:"+shopItems.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllShop() {
        shopItems = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(ShopActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "get_all_shop.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("items");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo MyOrders", response);
                if (status == 1) {
                    try {
                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject json = courses.getJSONObject(i);
                            shopItems.add(new ShopItem(
                                    json.getInt("id"),//0
                                    json.getString("image"),//1
                                    json.getString("name"),//2
                                    json.getString("price"),//3
                                    json.getString("mrp"),//4
                                    json.getString("gst"),//5
                                    json.getString("description"),//6
                                    json.getString("category"),//7
                                    json.getString("sub_category"),//8
                                    json.getString("tags")//9
                            ));

                        }

                    } catch (Exception e) {
                        //Toast.makeText(ShopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }

                } else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "fetchLocationListing: " + e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Volley error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);
                popular_recycler.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shopAdapter = new ShopAdapter(ShopActivity.this, shopItems);
                popular_recycler.setAdapter(shopAdapter);
                Log.d("respo MyOrders", String.valueOf(shopItems));
                //Toast.makeText(ShopActivity.this, "items:"+shopItems.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();

        //fav count
        countLoopMethod();

        shimmerFrameLayout.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        getAllShop();
        cartCount = dbHelper.countCart();
        if (cartCount == 0) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + cartCount);
        }
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        //fav count
        countLoopMethod();
//        shimmerFrameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        popular_recycler.setVisibility(View.GONE);
//        shimmerFrameLayout.startShimmer();

        getAllCatagory();
        getAllShop();
        cartCount = dbHelper.countCart();
        if (cartCount == 0) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + cartCount);
        }


    }
}
