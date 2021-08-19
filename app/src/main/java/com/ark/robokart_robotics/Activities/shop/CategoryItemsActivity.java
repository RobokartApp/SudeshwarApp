package com.ark.robokart_robotics.Activities.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Fragments.shop.FavListFragment;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryItemsActivity extends AppCompatActivity {
    ImageView back_btn;
    ImageView cart_btn,wishlist;
    TextView cat_title;
    ShopAdapter shopAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<ShopItem> shopItems;
    TextView badge,favTv;
    DBHelper dbHelper;
    int cartCount,favCount;

    String category_name;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_category_items);
        this.progressBar = (ProgressBar) findViewById(R.id.popular_progress_category_items);
        this.recyclerView = (RecyclerView) findViewById(R.id.popular_recycler_category_items);
        this.cat_title = (TextView) findViewById(R.id.category_name);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.cart_btn = (ImageView) findViewById(R.id.cart_btn);

        progressBar.setVisibility(View.VISIBLE);
        category_name = getIntent().getStringExtra("name");

        this.cat_title.setText(category_name);
        favTv=findViewById(R.id.cart_fav);
        badge=findViewById(R.id.cart_badge);
        wishlist=findViewById(R.id.wishlist_btn);

        dbHelper=new DBHelper(this);
        cartCount=dbHelper.countCart();
        if (cartCount==0){
            badge.setVisibility(View.GONE);
        }else{
            badge.setVisibility(View.VISIBLE);
            badge.setText(""+cartCount);
        }
        extractedFavCount();

//        getAllDetailsByCategory();
        getDataListWithLimit(category_name);
        /*shopItems=new ArrayList<>();
        
        shopItems.add(new ShopItem(1, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "100 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits \nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo non mollis id cursus. Eu euismod faucibus in leo\n\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Ornare leo \n\nnon mollis id cursus. Eu euismod faucibus in leo", "cat1", "sub1", "tag"));
        shopItems.add(new ShopItem(2, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "200 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"));
        shopItems.add(new ShopItem(3, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "300 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"));
        shopItems.add(new ShopItem(4, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "500 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"));
        shopItems.add(new ShopItem(5, "https://upload.wikimedia.org/wikipedia/commons/4/4f/Pcb33.430-g1.jpg", "400 pin breadboard", "1234", "2312", "233", "General purpose pcb board which are used to build the small to biggest circuits ", "cat1", "sub1", "tag"));

        this.recyclerView.setAdapter(new ShopAdapter(this, shopItems));
        this.cart_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CategoryItemsActivity.this.startActivity(new Intent(CategoryItemsActivity.this, CartActivity.class));
            }
        });*/
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

        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CategoryItemsActivity.this.onBackPressed();
            }
        });
    }

    private void extractedFavCount() {
        favCount = dbHelper.countFev();
        if (favCount == 0) {
            favTv.setVisibility(View.GONE);
        } else {
            favTv.setVisibility(View.VISIBLE);
            favTv.setText("" + favCount);
        }
    }

    private void getDataListWithLimit(String category_name) {
        shopItems=new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://robokart.com/Api/get_item_detail_by_category.php?apicall=get_item_details_by_category",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
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

                            if (shopItems.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Data is empty...!", Toast.LENGTH_SHORT).show();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                shopAdapter = new ShopAdapter(CategoryItemsActivity.this, shopItems);
                                recyclerView.setAdapter(shopAdapter);
                            }
                        }catch (Exception e){
                            Log.e( "Volley Exception...!",e.getMessage());
                            Toast.makeText(getApplicationContext(), "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();

                        try {
                            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "No Connection/Communication Error!", Toast.LENGTH_SHORT).show();
                            } else if (volleyError instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), "Authentication/ Auth Error!", Toast.LENGTH_SHORT).show();
                            } else if (volleyError instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                            } else if (volleyError instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                            } else if (volleyError instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("categoty", category_name);
                return params;
            }
        };

        Volley.newRequestQueue(CategoryItemsActivity.this).add(stringRequest);

    }
    /*private void getAllDetailsByCategory() {
            shopItems = new ArrayList<>();
            RequestQueue requestQueue = Volley.newRequestQueue(CategoryItemsActivity.this);
            StringRequest request = new StringRequest(Request.Method.POST, "https://robokart.com/Api/get_item_detail_by_category.php", response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courses = jsonObject.getJSONArray("items");
                    int status = jsonObject.getInt("success_code");
                    String error_msg = jsonObject.getString("error_msg");
                    Log.d("respo MyOrders", response);
                    Toast.makeText(getApplicationContext(), "respons: "+String.valueOf(status), Toast.LENGTH_SHORT).show();
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
                            Log.e("Volley Exception: ", e.getMessage());
                        }

                    } else if (status == 0) {
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Volley Exception: ", "fetchLocationListing: " + e.getMessage());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Volley Error: ", "Volley error: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("categoryOb",category_name);
                    return params;
                }
            };
            requestQueue.add(request);
            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
                @Override
                public void onRequestFinished(Request<String> request) {
                    shopAdapter = new ShopAdapter(getApplicationContext(), shopItems);
                    recyclerView.setAdapter(shopAdapter);
                    Log.d("respo MyOrders", String.valueOf(shopItems));
                    //Toast.makeText(ShopActivity.this, "items:"+shopItems.size(), Toast.LENGTH_SHORT).show();
                }
            });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        cartCount=dbHelper.countCart();
        if (cartCount==0){
            badge.setVisibility(View.GONE);
        }else{
            badge.setVisibility(View.VISIBLE);
            badge.setText(""+cartCount);
        }
    }

}
