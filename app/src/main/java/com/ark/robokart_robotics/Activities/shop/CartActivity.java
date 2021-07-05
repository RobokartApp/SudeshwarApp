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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.ark.robokart_robotics.Activities.shop.CartAdapter;
import com.ark.robokart_robotics.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    TextView total;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_cart);
        this.sum1 = CartAdapter.sumMrp;
        this.sum2 = CartAdapter.sumPrice;
        DBHelper dBHelper = new DBHelper(this);
        this.dbHelper = dBHelper;
        Cursor res = dBHelper.getData();
        if (res.getCount() != 0) {
            init();
            listener();
            this.shopItems = new ArrayList<>();
            while (res.moveToNext()) {
                Log.e("cart val", "" + res.getInt(0));
                this.shopItems.add(new CartItem(res.getInt(0), res.getString(2), res.getString(1), res.getString(5), res.getString(4), res.getInt(3)));
            }

            CartAdapter.OnItemClickListener r1 = new CartAdapter.OnItemClickListener() {
                public void onSelect(int positionIn, ArrayList<CartItem> list, AdapterView<?> parent, int upPosition) {
                    int qty = Integer.parseInt(parent.getItemAtPosition(positionIn).toString());
                    CartActivity.this.dbHelper.updateCart(Integer.valueOf(list.get(upPosition).getId()), "" + qty);
                    double rateP = Double.parseDouble(list.get(upPosition).getOffer_price()) * ((double) qty);
                    double rateM = Double.parseDouble(list.get(upPosition).getMrp()) * ((double) qty);
                    Log.e("qty val", "" + rateP);
                    CartActivity cartActivity = CartActivity.this;
                    cartActivity.sum2 = cartActivity.sum2 - Double.parseDouble(list.get(upPosition).getOffer_price());
                    CartActivity.this.sum1 -= Double.parseDouble(list.get(upPosition).getMrp());
                    Log.e("on sum minus vals", "mrp: " + CartActivity.this.sum1 + "\nprice: " + CartActivity.this.sum2);
                    CartActivity cartActivity2 = CartActivity.this;
                    cartActivity2.sum1 = cartActivity2.sum1 + rateM;
                    CartActivity cartActivity3 = CartActivity.this;
                    cartActivity3.sum2 = cartActivity3.sum2 + rateP;
                    Log.e("on sum plus vals", "mrp: " + CartActivity.this.sum1 + "\nprice: " + CartActivity.this.sum2);
                    CartActivity.this.Update();
                    CartActivity.this.preQty = qty;
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
        }
    }

    /* access modifiers changed from: private */
    public void Update() {
        double payable = this.sum2;
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
                CartActivity.this.startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView((int) R.layout.layout_apply_coupon);
        final EditText enter_coupon = (EditText) bottomSheetDialog.findViewById(R.id.enter_coupon);
        ArrayList<String> cpn = new ArrayList<>();
        ArrayList<String> info = new ArrayList<>();
        cpn.add("HEY20");
        cpn.add("RBKWEL10");
        info.add("-Get 20% off on this code.");
        info.add("10 % descount with this coupon.");
        ((RecyclerView) bottomSheetDialog.findViewById(R.id.coupon_recycler)).setAdapter(new CouponAdapter(this, cpn, info));
        ((TextView) bottomSheetDialog.findViewById(R.id.apply_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (enter_coupon.getText().toString().isEmpty()) {
                    enter_coupon.setError("Enter Coupon code!");
                    enter_coupon.requestFocus();
                    return;
                }
                Toast.makeText(CartActivity.this, "Your coupon has been applied!", Toast.LENGTH_SHORT).show();
            }
        });
        ((TextView) bottomSheetDialog.findViewById(R.id.apply_text_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }
}
