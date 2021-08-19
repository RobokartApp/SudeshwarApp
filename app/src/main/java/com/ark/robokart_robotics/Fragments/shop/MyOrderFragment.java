package com.ark.robokart_robotics.Fragments.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.shop.CheckoutActivity;
import com.ark.robokart_robotics.Activities.shop.MyOrderAdapter;
import com.ark.robokart_robotics.Activities.shop.OrderItem;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.MyPostModel;
import com.ark.robokart_robotics.R;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrderFragment extends Fragment {

    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    ProgressBar progressBar;
    ImageView back_btn;
    String user_id,TAG="MyOrderFrag";
    RecyclerView order_recycler;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView no_orders;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_id = getContext().getSharedPreferences("userdetails", 0).getString("customer_id", "");
        init(view);
        listener();
        getMyOrders();
    }

    private void init(View v) {
        no_orders=v.findViewById(R.id.no_orders);
        progressBar=v.findViewById(R.id.progressBar);
        back_btn=v.findViewById(R.id.back_btn);
        order_recycler=v.findViewById(R.id.orders_recycler);
        swipeRefreshLayout=v.findViewById(R.id.swipeToRefresh);
    }

    private void listener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getMyOrders();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(MyOrderFragment.this).commit();
            }
        });
    }
    public void getMyOrders() {
        ArrayList<OrderItem> orderItems=new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST +"get_orders.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("my_order");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo MyOrders",response);
                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            orderItems.add( new OrderItem(
                                    json.getString("order_id"),//0
                                    json.getString("order_img"),//1
                                    json.getString("order_name"),//2
                                    json.getString("order_delivery")//3
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
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);
                MyOrderAdapter myOrderAdapter=new MyOrderAdapter(getContext(),orderItems);
                order_recycler.setAdapter(myOrderAdapter);
                if (orderItems.size()>0){
                    no_orders.setVisibility(View.GONE);
                }else
                    no_orders.setVisibility(View.VISIBLE);

            }
        });
    }
}