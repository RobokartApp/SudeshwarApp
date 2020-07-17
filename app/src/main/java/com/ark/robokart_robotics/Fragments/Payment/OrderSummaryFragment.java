package com.ark.robokart_robotics.Fragments.Payment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.CourseDetails.CourseDetailsActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.MyCoursesModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.example.vimeoplayer2.vimeoextractor.OnVimeoExtractionListener;
import com.example.vimeoplayer2.vimeoextractor.VimeoExtractor;
import com.example.vimeoplayer2.vimeoextractor.VimeoVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import carbon.widget.Button;
import carbon.widget.TextView;

public class OrderSummaryFragment extends Fragment {

    private static final String TAG = "Order Summary";
    private Button billing_btn,apply_btn;

    private ImageButton minus_btn, plus_btn;

    private TextView quantity_txt, price_edt, total_amount_txt, total_cost_txt,disc_txt;
    private EditText code_edt;

    private   int quantity_count = 1;
    private RequestQueue requestQueue;
String[] code,exp,user;int[] min,perc;
int course_price,total,discount;
Context context;
    public OrderSummaryFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ordersummaryfragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context=getActivity().getApplicationContext();
        init(view);
price_edt.setText("₹ "+CourseDetailsActivity.course_online_price);
        total_amount_txt.setText("₹ "+CourseDetailsActivity.course_online_price);
        total_cost_txt.setText("₹ "+CourseDetailsActivity.course_online_price);
        listeners();
    }

    //Initialise UI
    public void init(View v){
        course_price=Integer.parseInt(CourseDetailsActivity.course_online_price);
        total=Integer.parseInt(CourseDetailsActivity.course_online_price);
        code=new String[212];
        perc=new int[212];
        min=new int[212];
        exp=new String[212];
        user=new String[212];
        billing_btn = v.findViewById(R.id.billing_btn);
        apply_btn = v.findViewById(R.id.apply_btn);
        code_edt = v.findViewById(R.id.code_edt);
        minus_btn = v.findViewById(R.id.minus_btn);
        plus_btn = v.findViewById(R.id.plus_btn);
        quantity_txt = v.findViewById(R.id.quantity_txt);
        price_edt = v.findViewById(R.id.price);
        total_amount_txt = v.findViewById(R.id.total_amount_txt);
        total_cost_txt = v.findViewById(R.id.total_cost_txt);
        disc_txt=v.findViewById(R.id.discount_amount_txt);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    //Event Listeners
    public void listeners(){

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "Coupan: "+code[1]+" | Code: "+code_edt.getText(), Toast.LENGTH_SHORT).show();
                String code=code_edt.getText().toString();
                getCoupon(code);

                /*int i=0;int ok=0;
                for(String kupan:code){
                    Toast.makeText(context, ""+i+". "+kupan, Toast.LENGTH_SHORT).show();
                    if(kupan==code_edt.getText().toString()){
                        Toast.makeText(context, "kupan: "+kupan, Toast.LENGTH_SHORT).show();
                        discount=total*perc[i]/100;
                        disc_txt.setText(discount);
                        total-=discount;
                        total_cost_txt.setText(total);
                        ok=1;break;
                    }
                    i++;
                }*/
                //if(ok==1)Toast.makeText(context, "Yeahhh!", Toast.LENGTH_SHORT).show();
                //else Toast.makeText(context, "Sorry coupon may be expired or invalid", Toast.LENGTH_SHORT).show();
            }
        });

        billing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillingDetailsFragment billingDetailsFragment = new BillingDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                        .replace(R.id.paymentFragment, billingDetailsFragment, "billing")
                        .addToBackStack("order")
                        .commit();
            }
        });

        String price = price_edt.getText().toString();
        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementQuantity();
                quantity_txt.setText(String.valueOf(quantity_count));

                String number  = price.replaceAll("[^0-9]", "");

                int val_price = Integer.parseInt(number);

                int val = val_price * quantity_count;
                price_edt.setText("₹ "+val);
                total_cost_txt.setText("₹ "+val);
                total_amount_txt.setText("₹ "+val);
            }
        });

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decerementQuantity();
                quantity_txt.setText(String.valueOf(quantity_count));

                String number  = price.replaceAll("[^0-9]", "");

                int val_price = Integer.parseInt(number);

                int val = val_price * quantity_count;
                price_edt.setText("₹ "+val);
                total_cost_txt.setText("₹ "+val);
                total_amount_txt.setText("₹ "+val);
            }
        });

    }


    public void incrementQuantity(){
        quantity_count++;
    }

    public void decerementQuantity(){
        if(quantity_count != 1)
        quantity_count--;
    }
    public void getCoupon(String code){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.coupons_api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            discount = jsonObject.getInt("amount");
                            int status = jsonObject.getInt("success_code");
                            String error_msg = jsonObject.getString("error_msg");

                            if (status == 1) {
                                int val=total-discount;
                                disc_txt.setText(""+discount);
                                total_cost_txt.setText("₹ "+val);
                                Toast.makeText(context, "Yeahhhh!", Toast.LENGTH_SHORT).show();
                            }else if (status == 0) {
                                Toast.makeText(context, error_msg, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "fetchLocationListing: "+e.getMessage());

                        }
                        //Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e(TAG, "Error in payment API", error);
                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("code",""+code);
                parameters.put("price",""+course_price);
                parameters.put("usertype","user-maker");
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
        /*
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.coupons_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                discount = jsonObject.getInt("amount");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");

                if (status == 1) {
                    int val=total-discount;
                    total_cost_txt.setText("₹ "+val);
                    Toast.makeText(context, "Yeahhhh!", Toast.LENGTH_SHORT).show();
                }else if (status == 0) {
                    Toast.makeText(context, error_msg, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                parameters.put("code",""+code);
                parameters.put("price",""+course_price);
                parameters.put("usertype","user-maker");
                return parameters;
            }
        };
        requestQueue.add(request);*/
    }
}
