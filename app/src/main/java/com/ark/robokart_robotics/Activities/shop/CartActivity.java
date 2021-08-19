package com.ark.robokart_robotics.Activities.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.shop.CartAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    /* renamed from: df */
    private static DecimalFormat f4df = new DecimalFormat("0.00");
    TextView apply_coupon;
    ImageView back_btn;
    TextView cart_disc;
    RecyclerView cart_recycler;
    TextView checkout;
    TextView cpn_disc;
    DBHelper dbHelper;
    TextView del_charge;
    TextView gst;

    /* renamed from: in */
    int f5in = 0;
    CartAdapter.OnItemClickListener listener;
    int preQty = 1;
    TextView price_tot;
    TextView product_cost;
    CartAdapter shopAdapter;
    ArrayList<CartItem> shopItems;
    TextView sub_tot;
    double sum1 = 0.0d;
    double sum2 = 0.0d;
    double payable;
    TextView total,no_items;
    ArrayList<CouponItem> couponItems;
    ProgressBar progressBar;
    String used_coupon;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_cart);
        this.sum1 = CartAdapter.sumMrp;
        this.sum2 = CartAdapter.sumPrice;
        DBHelper dBHelper = new DBHelper(this);
        this.dbHelper = dBHelper;
        Cursor res = dBHelper.getData();
        used_coupon="NA";
        init();
        listener();

        if (res.getCount() != 0) {
            no_items.setVisibility(View.GONE);
            cart_recycler.setVisibility(View.VISIBLE);
            this.shopItems = new ArrayList<>();
            while (res.moveToNext()) {
                Log.e("cart val", "" + res.getInt(0));
                this.shopItems.add(new CartItem(res.getInt(0), res.getString(2),
                        res.getString(1), res.getString(5), res.getString(4),
                        res.getInt(3),res.getString(6)));
            }

            CartAdapter.OnItemClickListener r1 = new CartAdapter.OnItemClickListener() {
                public void onSelect(int positionIn, ArrayList<CartItem> list, AdapterView<?> parent, int upPosition) {
                    int qty = Integer.parseInt(parent.getItemAtPosition(positionIn).toString());
                    CartActivity.this.dbHelper.updateCart(Integer.valueOf(list.get(upPosition).getId()), "" + qty);
                    double rateP = Double.parseDouble(list.get(upPosition).getOffer_price()) * ((double) qty);
                    double rateM = Double.parseDouble(list.get(upPosition).getMrp()) * ((double) qty);
                    for (int i=0;i<list.size();i++) {
                        if (i!=upPosition) {
                            rateP += Double.parseDouble(list.get(i).getOffer_price()) * ((double) list.get(i).getQty());
                            rateM += Double.parseDouble(list.get(i).getMrp()) * ((double) list.get(i).getQty());
                        }
                    }

                    Log.e("qty val", "" + rateP);
                   sum1 =  rateM;
                    sum2 = rateP;
                    Log.e("on sum plus vals", "mrp: " + CartActivity.this.sum1 + "\nprice: " + CartActivity.this.sum2);
                    CartActivity.this.Update();
                    CartActivity.this.preQty = qty;
                    //Toast.makeText(CartActivity.this, ""+list.get(upPosition).getQty(), Toast.LENGTH_SHORT).show();

                }

                public void onItemClick(final int positon, final ArrayList<CartItem> list) {
                    Log.e("before delete vals", "mrp: " + CartActivity.this.sum1 + "\nprice: " + CartActivity.this.sum2);
                    new AlertDialog.Builder(CartActivity.this).setMessage((CharSequence) "Are you sure to delete?").setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            CartActivity.this.sum2 -= Double.parseDouble(((CartItem) list.get(positon)).getOffer_price());
                            CartActivity.this.sum1 -= Double.parseDouble(((CartItem) list.get(positon)).getMrp());
                            CartActivity.this.dbHelper.deleteContact(Integer.valueOf(((CartItem) list.get(positon)).getId()));
                            list.remove(positon);
                            CartActivity.this.shopAdapter.notifyDataSetChanged();
                            Log.e("deleted after vals", "mrp: " + CartActivity.this.sum1 + "\nprice: " + CartActivity.this.sum2);
                        Update();
                        }
                    }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).show();
                }
            };
            this.listener = r1;
            CartAdapter cartAdapter = new CartAdapter(this, this.shopItems, r1);
            this.shopAdapter = cartAdapter;
            this.cart_recycler.setAdapter(cartAdapter);
            new CountDownTimer(100, 100) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    CartActivity.this.sum1 = CartAdapter.sumMrp;
                    CartActivity.this.sum2 = CartAdapter.sumPrice;
                   Update();
                }
            }.start();
        }else{
            apply_coupon.setVisibility(View.GONE);
            checkout.setEnabled(false);
            no_items.setVisibility(View.VISIBLE);
            cart_recycler.setVisibility(View.GONE);
        }
    }

    /* access modifiers changed from: private */
    public void Update() {
         payable = this.sum2;
        TextView textView = this.price_tot;
        textView.setText("₹ " + this.sum1);
        double cartDisc = this.sum1 - this.sum2;
        TextView textView2 = this.cart_disc;
        textView2.setText("-₹ " + f4df.format(cartDisc));
        if (this.sum2 >= 1000.0d) {
            this.del_charge.setText("Free");
            this.del_charge.setTextColor(Color.parseColor("#065D00"));
        } else {
            this.del_charge.setText("40");
            this.del_charge.setTextColor(Color.parseColor("#3F3F3F"));
            payable += 40.0d;
            sum2+=40d;
        }
        double subTot = (this.sum2 * 100.0d) / 118.0d;
        TextView textView3 = this.product_cost;
        textView3.setText("₹ " + f4df.format(subTot));
        TextView textView4 = this.sub_tot;
        textView4.setText("₹ " + this.sum2);
        TextView textView5 = this.gst;
        textView5.setText("₹ " + f4df.format(this.sum2 - subTot));
        f4df.format(payable);
        TextView textView6 = this.total;
        textView6.setText("₹ " + payable);
        Log.e("Updated vals", "mrp: " + this.sum1 + "\nprice: " + this.sum2);
    }

    private void init() {
        no_items=findViewById(R.id.no_items);
        this.product_cost = (TextView) findViewById(R.id.product_cost_price);
        this.sub_tot = (TextView) findViewById(R.id.subtotal_price);
        this.gst = (TextView) findViewById(R.id.gst_amount);
        this.total = (TextView) findViewById(R.id.payable_amnt);
        this.price_tot = (TextView) findViewById(R.id.price_cart_total);
        this.cart_disc = (TextView) findViewById(R.id.price_cart_discount);
        this.del_charge = (TextView) findViewById(R.id.price_del_charge);
        this.cpn_disc = (TextView) findViewById(R.id.coupon_disc_amount);
        this.apply_coupon = (TextView) findViewById(R.id.apply_coupon);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.checkout = (TextView) findViewById(R.id.checkout_btn);
        this.cart_recycler = (RecyclerView) findViewById(R.id.cart_item_recycle);
    }

    private void listener() {
        this.apply_coupon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        this.checkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("total",sum2);
                intent.putExtra("coupon",used_coupon);
                CartActivity.this.startActivity(intent);
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    RecyclerView couponRecycler;
    /* access modifiers changed from: private */
    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView((int) R.layout.layout_apply_coupon);
        final EditText enter_coupon = (EditText) bottomSheetDialog.findViewById(R.id.enter_coupon);

       couponRecycler=bottomSheetDialog.findViewById(R.id.coupon_recycler);
       progressBar=bottomSheetDialog.findViewById(R.id.progressBar);

        ((TextView) bottomSheetDialog.findViewById(R.id.apply_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (enter_coupon.getText().toString().isEmpty()) {
                    enter_coupon.setError("Enter Coupon code!");
                    enter_coupon.requestFocus();
                    return;
                }
                Toast.makeText(CartActivity.this, "Coupon code is not valid or expired!", Toast.LENGTH_SHORT).show();
            }
        });
        ((TextView) bottomSheetDialog.findViewById(R.id.apply_text_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        bottomSheetDialog.show();
        getCoupons(bottomSheetDialog);
    }

    public void getCoupons(BottomSheetDialog bottomSheetDialog) {
        couponItems=new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST +"get_shop_coupons.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("coupons");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.e("respo getCoupons",response);
                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            couponItems.add(new CouponItem(
                                    json.getString("code"),
                                    json.getString("detail"),
                                    json.getInt("percent"),
                                    json.getInt("minimum")
                            ));
                        }

                    }
                    catch (Exception e){
                        //Toast.makeText(ShopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("CartAct",e.getMessage());
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("CartAct", "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("CartAct", "Volley error: "+error.getMessage());
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
                couponRecycler.setAdapter(new CouponAdapter(CartActivity.this, couponItems, new CouponAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int i) {
                        if(sum2>couponItems.get(i).getMinimum()){
                            double perc=couponItems.get(i).getPercent();
                            double discAm=(perc/100)*sum2;
                            double am=sum2-discAm;
                            Log.e("coupon click","perc:"+perc+"\nam:"+am+"\ndisc:"+discAm);
                            cpn_disc.setText("-₹ " +f4df.format(discAm));
                            total.setText("₹ " +f4df.format(am));
                            sum2=am;
                            used_coupon=couponItems.get(i).getCode();
                            bottomSheetDialog.dismiss();
                            Toast.makeText(CartActivity.this, "Coupon code has been applied successfully!", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(CartActivity.this, "Coupon code is invalid or expired!", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });
    }
}
