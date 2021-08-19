package com.ark.robokart_robotics.Activities.shop;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    DecimalFormat df = new DecimalFormat("0.00");
    String TAG="OrderDetailAct",user_id,id;
    Context context;
    ImageView back_btn;
    ProgressBar progressBar;
    RecyclerView order_item_recycler;
    TextView track_btn,del_date_text,address_text,order_date_text,order_id_text,item_total_text,gst_text,order_total_text,pay_mode;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_order_detail);
        context=this;
        user_id = this.getSharedPreferences("userdetails", 0).getString("customer_id", "");
        track_link="NA";

        init();
        listener();

        Intent intent=getIntent();
        id=intent.getStringExtra("order_id");
        order_id_text.setText(id);
        getOrderDetail();
    }

    private void init() {
        progressBar=findViewById(R.id.progressBar);
        order_item_recycler=findViewById(R.id.order_items_recycler);
        back_btn=findViewById(R.id.back_btn);
        del_date_text=findViewById(R.id.delivered_date_tv);
        address_text=findViewById(R.id.order_address);
        order_date_text=findViewById(R.id.order_date);
        order_id_text=findViewById(R.id.order_id);
        item_total_text=findViewById(R.id.items_total);
        gst_text=findViewById(R.id.gst_tax);
        order_total_text=findViewById(R.id.order_total);
        pay_mode=findViewById(R.id.payment_method);
        track_btn=findViewById(R.id.track_btn);
    }

    private void listener() {
        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!track_link.equalsIgnoreCase("NA")) {
                    Uri uri = Uri.parse(track_link); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else
                    Toast.makeText(context, "Tracking link not available", Toast.LENGTH_SHORT).show();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    String del_status,order_address,order_date2,item_total2,gst2,order_total2,pay_method,track_link;
    public void getOrderDetail() {
        ArrayList<OrderDetailItem> orderItems=new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST +"get_order_detail.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("my_order");
                int status = jsonObject.getInt("success_code");
                del_status=jsonObject.getString("order_status");
                order_address=jsonObject.getString("order_address");
                order_date2=jsonObject.getString("order_date");
                item_total2=jsonObject.getString("item_total");
                gst2=jsonObject.getString("gst");
                order_total2=jsonObject.getString("order_total");
                pay_method=jsonObject.getString("pay_method");
                track_link=jsonObject.getString("track_link");

                del_date_text.setText(del_status);
                address_text.setText(order_address);
                order_date_text.setText(order_date2);
                item_total_text.setText("₹ " +df.format(Double.parseDouble(item_total2)));
                gst_text.setText("₹ " +df.format(Double.parseDouble(gst2)));
                order_total_text.setText("₹ " +df.format(Double.parseDouble(order_total2)));
                pay_mode.setText(pay_method);

                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo MyOrders",response);
                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            orderItems.add( new OrderDetailItem(
                                    json.getString("item_img"),//0
                                    json.getString("item_name"),//1
                                    json.getString("item_price"),//2
                                    json.getString("item_mrp"),//3
                                    json.getInt("item_qty")//3
                            ));
                        }
                    }
                    catch (Exception e){
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id",user_id);
                parameters.put("order_id",id);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);
                OrderDetailAdapter myOrderAdapter=new OrderDetailAdapter(context,orderItems);
                order_item_recycler.setAdapter(myOrderAdapter);
            }
        });
    }
}
