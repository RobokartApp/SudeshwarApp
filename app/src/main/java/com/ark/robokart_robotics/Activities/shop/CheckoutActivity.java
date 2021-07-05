package com.ark.robokart_robotics.Activities.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ark.robokart_robotics.Activities.shop.AddressAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity {
    String TAG = "CheckoutAct";
    ArrayList<String> addressAr;
    ImageView back_btn;
    TextView change_btn;
    ArrayList<String> idAr;
    ArrayList<String> nameAr;
    ArrayList<String> phoneAr;
    TextView selected_adr;
    String user_id;
    RadioGroup payment_rg;
    CheckBox gst_check;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_checkout);
        this.user_id = getSharedPreferences("userdetails", 0).getString("customer_id", "");
        init();
        listener();
        getAddress();
        getAllAddress();
    }

    private void init() {
        gst_check=findViewById(R.id.gst_checkbox);
        payment_rg=findViewById(R.id.payment_rg);
        this.selected_adr = (TextView) findViewById(R.id.selected_address);
        this.back_btn = (ImageView) findViewById(R.id.back_btn);
        this.change_btn = (TextView) findViewById(R.id.change_btn_tv);
    }

    private void listener() {
        gst_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
                    Toast.makeText(CheckoutActivity.this, "COD", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CheckoutActivity.this, "Razorpay", Toast.LENGTH_SHORT).show();
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
                CheckoutActivity.this.sendAddress(editText.getText().toString().trim(),
                        editText2.getText().toString().trim() + ", " + editText3.getText().toString().trim() +
                                ", " + editText4.getText().toString().trim() + ", " + editText5.getText().toString().trim(),
                        editText6.getText().toString().trim(), editText7.getText().toString().trim(), bottomSheetDialog);

            }
        });

        bottomSheetAddress.show();
    }

    /* access modifiers changed from: private */
    public void getAddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue2 = requestQueue;
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
}
