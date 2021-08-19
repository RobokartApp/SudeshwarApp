package com.ark.robokart_robotics.Activities.shop;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.shop.AddressAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.http.POST;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    String TAG = "CheckoutAct";
    ArrayList<String> addressAr;
    DecimalFormat df=new DecimalFormat("0.00");
    ImageView back_btn;
    TextView change_btn;
    ArrayList<String> idAr;
    ArrayList<String> nameAr;
    ArrayList<String> phoneAr,itemImgs,itemNames,itemQty,itemMrp,itemPrice;
    TextView selected_adr,place_order;
    String user_id;
    RadioGroup payment_rg;
    CheckBox gst_check;
    String gstin_s="",business_s="",total,payment_mode="razorpay",coupon;
    DBHelper dbHelper;
    Checkout checkout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_checkout);
        this.user_id = getSharedPreferences("userdetails", 0).getString("customer_id", "");

        DBHelper dBHelper = new DBHelper(this);
        this.dbHelper = dBHelper;
        Cursor res = dBHelper.getData();

        Checkout.preload(getApplicationContext());
        checkout = new Checkout();

        Intent extraIntent=getIntent();
        total=""+df.format(extraIntent.getDoubleExtra("total",0d));
        coupon=extraIntent.getStringExtra("coupon");
        double price=Double.parseDouble(total)*100;
        Log.e("CheckAct","total:"+total+" & price:"+price);

        itemNames=new ArrayList<>();
        itemImgs=new ArrayList<>();
        itemQty=new ArrayList<>();
        itemMrp=new ArrayList<>();
        itemPrice=new ArrayList<>();
        if (res.getCount() != 0) {

            while (res.moveToNext()) {
                itemNames.add(res.getString(1));
                itemImgs.add(res.getString(2));
                itemQty.add(res.getString(3));
                itemMrp.add(res.getString(4));
                itemPrice.add(res.getString(5));
            }
        }

        init();
        listener();
        getAddress();
        getAllAddress();

        place_order.setText("PAY AND PLACE ORDER");
    }

    private void init() {
        place_order=findViewById(R.id.place_order);
        gst_check=findViewById(R.id.gst_checkbox);
        payment_rg=findViewById(R.id.payment_rg);
        this.selected_adr = (TextView) findViewById(R.id.selected_address);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.change_btn = (TextView) findViewById(R.id.change_btn_tv);
    }

    private void listener() {
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place_order.getText().toString().equalsIgnoreCase("pay and place order"))
                    startPayment();
                else
                    placeOrder("NA");
            }
        });
        gst_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showGstDialog();
            }
        });

        payment_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (checkedRadioButton.getText().toString().equalsIgnoreCase(" Cash On Delivery"))
                {
                    place_order.setText("PLACE ORDER");
                    payment_mode="COD";
                    //Toast.makeText(CheckoutActivity.this, "COD", Toast.LENGTH_SHORT).show();
                }else{
                    payment_mode="razorpay";
                    place_order.setText("PAY AND PLACE ORDER");
                    //Toast.makeText(CheckoutActivity.this, "Razorpay", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.change_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckoutActivity.this.showBottomSheetDialog();
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckoutActivity.this.onBackPressed();
            }
        });
    }

    String isGST="0";boolean isOrder=false;JSONObject object;

    public void placeOrder(String pay_id) {

        if(!gstin_s.isEmpty()){
            isGST="1";
        }
        try {
            object=new JSONObject();
            object.put("images",itemImgs);  //0
            object.put("names",itemNames);  //1
            object.put("qty",itemQty);      //2
            object.put("mrp",itemMrp);      //3
            object.put("price",itemPrice);  //4

        }catch (Exception e){
            Log.e("JSON exception",e.getMessage());
        }
        GsonBuilder gsonBuilder;
        gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        String op=gson.toJson(object);
        Log.e("Place order","came in place order method"+object.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.POST, ApiConstants.HOST + "submit_order.php", new Response.Listener() {
            public final void onResponse(Object obj) {
                Log.e("placeOrder Respo",obj.toString());
                if (obj.toString().equalsIgnoreCase("success"))
                    isOrder=true;
                else
                    isOrder=false;
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                String str = CheckoutActivity.this.TAG;
                Log.d(str, "Volley error: " + error.getMessage());
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("cust_id",user_id);
                parameters.put("item_details", op);//object.toString());
                parameters.put("isGST", isGST);
                parameters.put("gstin", gstin_s);
                parameters.put("business_name", business_s);
                parameters.put("order_total", total);
                parameters.put("no_items", ""+itemNames.size());
                parameters.put("payment_mode", payment_mode);
                parameters.put("payment_id", pay_id);
                parameters.put("used_coupon",coupon);
                return parameters;
            }
        });
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            public void onRequestFinished(Request<String> request) {
                if (isOrder) {
                    showThanksOrder();
                    dbHelper.fireSql("delete from cart");
                }else {
                    Toast.makeText(CheckoutActivity.this, "Failed! Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void showGstDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        //ViewGroup viewGroup = mContext.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialog= LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.dialog_gst_invoice, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialog);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        TextView close=dialog.findViewById(R.id.close_btn);
        EditText gstin_et = dialog.findViewById(R.id.gstin);
        EditText busi_name_et = dialog.findViewById(R.id.business_name);

        Button submitButton = dialog.findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gstin_et.getText().toString().isEmpty() || gstin_et.getText().toString().equalsIgnoreCase(" ")){
                    gstin_et.setError("Enter GSTIN Number!");
                    gstin_et.requestFocus();
                }else if(busi_name_et.getText().toString().isEmpty() || busi_name_et.getText().toString().equalsIgnoreCase(" ")){
                    busi_name_et.setError("Enter Business Name!");
                    busi_name_et.requestFocus();
                }else{
                    gstin_s=gstin_et.getText().toString().trim();
                    business_s=busi_name_et.getText().toString().trim();
                    alertDialog.dismiss();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(gstin_s.isEmpty() || business_s.isEmpty()){
                    gst_check.setChecked(false);
                }else
                    gst_check.setChecked(true);
            }
        });

        alertDialog.show();

    }

    private void showThanksOrder() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        //ViewGroup viewGroup = mContext.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialog= LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.thanks_for_order, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialog);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        ImageView close=dialog.findViewById(R.id.close_btn);
        TextView view_order = dialog.findViewById(R.id.view_order_tv);
        TextView go_home = dialog.findViewById(R.id.back_to_home);

        view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CheckoutActivity.this, HomeActivity.class);
                intent.putExtra("order","ok");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(CheckoutActivity.this,ShopActivity.class));
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
                Intent intent=new Intent(getApplicationContext(),ShopActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        alertDialog.show();


    }

    /* access modifiers changed from: private */
    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView((int) R.layout.layout_change_address);
        ((RecyclerView) bottomSheetDialog.findViewById(R.id.coupon_recycler)).setAdapter(new AddressAdapter(this, this.idAr, this.nameAr, this.addressAr, this.phoneAr, new AddressAdapter.OnItemClickListener() {
            public void onItemClick(int position, int id) {
                CheckoutActivity.this.updateAddress(id, bottomSheetDialog);
            }
        }));
        ((TextView) bottomSheetDialog.findViewById(R.id.add_new_addr)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                CheckoutActivity.this.showBottomSheetAddress();
            }
        });
        ((TextView) bottomSheetDialog.findViewById(R.id.apply_text_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    /* access modifiers changed from: private */
    public void showBottomSheetAddress() {
        BottomSheetDialog bottomSheetAddress = new BottomSheetDialog(this);
        bottomSheetAddress.setContentView((int) R.layout.layout_new_address);
        EditText name = (EditText) bottomSheetAddress.findViewById(R.id.fullname);
        final EditText editText = name;
        final EditText editText2 = (EditText) bottomSheetAddress.findViewById(R.id.address_line1);
        final EditText editText3 = (EditText) bottomSheetAddress.findViewById(R.id.address_line2);
        final EditText editText4 = (EditText) bottomSheetAddress.findViewById(R.id.address_line3);
        final EditText editText5 = (EditText) bottomSheetAddress.findViewById(R.id.landmark);
        final EditText editText6 = (EditText) bottomSheetAddress.findViewById(R.id.pincode);
        //int r11 = r1;
        final EditText editText7 = (EditText) bottomSheetAddress.findViewById(R.id.mobile);
        EditText editText8 = name;
        TextView add = (TextView) bottomSheetAddress.findViewById(R.id.add_address_btn);
        final BottomSheetDialog bottomSheetDialog = bottomSheetAddress;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()){
                    name.requestFocus();
                    name.setError("Enter Full Name");
                }else if (editText2.getText().toString().isEmpty()){
                    editText2.requestFocus();
                    editText2.setError("Enter Address line 1");
                }else if (editText3.getText().toString().isEmpty()){
                    editText3.requestFocus();
                    editText3.setError("Enter Address line 2");
                }else if (editText6.getText().toString().isEmpty()){
                    editText6.requestFocus();
                    editText6.setError("Enter Pincode");
                }else if (editText7.getText().toString().isEmpty()){
                    editText7.requestFocus();
                    editText7.setError("Enter Mobile");
                }else {
                    CheckoutActivity.this.sendAddress(editText.getText().toString().trim(),
                            editText2.getText().toString().trim() + ", " + editText3.getText().toString().trim() +
                                    ", " + editText4.getText().toString().trim() + ", " + editText5.getText().toString().trim(),
                            editText6.getText().toString().trim(), editText7.getText().toString().trim(), bottomSheetDialog);
                }
            }
        });

        bottomSheetAddress.show();
    }

    /* access modifiers changed from: private */
    public void getAddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(1, "https://robokart.com/Api/get_address.php", new Response.Listener() {
            public final void onResponse(Object obj) {
                CheckoutActivity.this.lambda$getAddress$0$CheckoutActivity((String) obj);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                String str = CheckoutActivity.this.TAG;
                Log.d(str, "Volley error: " + error.getMessage());
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("customer_id", CheckoutActivity.this.user_id);
                parameters.put("selected", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                return parameters;
            }
        });
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            public void onRequestFinished(Request<String> request) {
            }
        });
    }

    public /* synthetic */ void lambda$getAddress$0$CheckoutActivity(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject adr = jsonObject.getJSONArray("address").getJSONObject(0);
            int status = jsonObject.getInt("success_code");
            Log.d("respo adr", response);
            if (status == 1) {
                try {
                    String name = adr.getString("name");
                    String addr = adr.getString("adr");
                    String pin = adr.getString("pin");
                    String mob = adr.getString("mob");
                    TextView textView = this.selected_adr;
                    textView.setText(name + ": " + addr + "," + pin + ",Mob." + mob);
                } catch (Exception e) {
                }
            }
        } catch (JSONException e2) {
            String str = this.TAG;
            Log.d(str, "fetchLocationListing: " + e2.getMessage());
        }
    }

    private void getAllAddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue2 = requestQueue;
        requestQueue.add(new StringRequest(1, "https://robokart.com/Api/get_address.php", new Response.Listener() {
            public final void onResponse(Object obj) {
                CheckoutActivity.this.lambda$getAllAddress$1$CheckoutActivity((String) obj);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                String str = CheckoutActivity.this.TAG;
                Log.d(str, "Volley error: " + error.getMessage());
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("customer_id", CheckoutActivity.this.user_id);
                parameters.put("selected", "0");
                return parameters;
            }
        });
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            public void onRequestFinished(Request<String> request) {
            }
        });
    }

    public /* synthetic */ void lambda$getAllAddress$1$CheckoutActivity(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray adrs = jsonObject.getJSONArray("address");
            this.nameAr = new ArrayList<>();
            this.addressAr = new ArrayList<>();
            this.phoneAr = new ArrayList<>();
            this.idAr = new ArrayList<>();
            int status = jsonObject.getInt("success_code");
            Log.d("respo adr", response);
            if (status == 1) {
                int i = 0;
                while (i < adrs.length()) {
                    try {
                        JSONObject json = adrs.getJSONObject(i);
                        this.nameAr.add(json.getString("name"));
                        ArrayList<String> arrayList = this.addressAr;
                        arrayList.add(json.getString("adr") + " " + json.getString("pin"));
                        this.phoneAr.add(json.getString("mob"));
                        this.idAr.add(json.getString("id"));
                        i++;
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        } catch (JSONException e2) {
            String str = this.TAG;
            Log.d(str, "fetchLocationListing: " + e2.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void sendAddress(String name, String address, String pincode, String mobile, BottomSheetDialog bottomSheetDialog) {
        final String str = name;
        final String str2 = address;
        final String str3 = pincode;
        final String str4 = mobile;
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "add_address.php", response -> {

                Log.e("SendAddress Respo",response);


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("name", str);
                parameters.put("address", str2);
                parameters.put("pincode", str3);
                parameters.put("mobile", str4);
                parameters.put("user_id", user_id);
                return parameters;
            }
        };
        RequestQueue requestQueue2=Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(request);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                bottomSheetDialog.cancel();
                CheckoutActivity.this.getAddress();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateAddress(int id, final BottomSheetDialog bottomSheetDialog) {

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + "update_address.php", response -> {

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", "" + id);
                parameters.put("user_id",user_id);
                return parameters;
            }
        };
        RequestQueue requestQueue2=Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(request);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                bottomSheetDialog.cancel();
                CheckoutActivity.this.getAddress();
            }
        });

    }

    public void startPayment() {
        checkout.setKeyID("rzp_test_EvXZMjKBLEZ4D2");
        //checkout.setKeyID("rzp_live_aqvt6qxDDA8LEx");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo_wall);


        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Robokart");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            Random random = new Random();
            String id="shop_rbk_"+random.nextInt(999999);
            options.put("description", id);
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("image", "https://robokart.com/app/app_robo.png");

            //  options.put("order_id", id);
            options.put("currency", "INR");


            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            double price=Double.parseDouble(total)*100;
            options.put("amount", df.format(price));

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.e("paymentData",s+"\n"+paymentData);
        placeOrder(s);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e(TAG,s);
        Toast.makeText(this, "Payment failed!", Toast.LENGTH_SHORT).show();
    }
}
