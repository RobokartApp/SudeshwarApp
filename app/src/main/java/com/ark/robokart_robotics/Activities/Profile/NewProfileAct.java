package com.ark.robokart_robotics.Activities.Profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Common.FileUtils;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.ImageUpload.RetrofitRequest;
import com.ark.robokart_robotics.ImageUpload.UploadAPI;
import com.ark.robokart_robotics.Model.ImageModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.myhexaville.smartimagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import carbon.widget.Button;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class NewProfileAct extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private static final String TAG = "NewProfileActivity";

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    ImageView back_btn;

    private EditText first_name_edt, last_name_edt, email_edt, phone_edt, bio_edt;

    private carbon.widget.TextView name_txt;

    private Button save_btn;

    private LottieAnimationView drawable_anim_fname, drawable_anim_lname, drawable_anim_email, drawable_anim_pass;

    private TextView textview_fname_error, textview_lname_error, textview_email_error, textview_pass_error;

    private ProgressBar uplaod_progressbar;

    private CircleImageView transparent;

    String user_id;
    public static int cust_id;

    String cust_img;

    String user_name;

    private ImageView upload_btn;

    private CircleImageView profile_image;

    public ProfileViewModel profileViewModel;

    private ImagePicker imagePicker;

    private carbon.widget.TextView tvGood;

    File file;

    Uri image_uri;

    private UploadAPI apiRequest;

    private TextView edit_btn,bio_txt;

    private boolean show;

    private final String img_base_url = "https://robokart.com/";

    File compressedImageFile;
    private String img_url;
    TextView no_story,no_doubt,no_coins;
    RequestQueue requestQueue;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile_new);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.stories));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ask_about));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ProfileStoryAdapter adapter = new ProfileStoryAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        init();
        listeners();
        getPostData();

    }
    public void init(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
       // progressBar=findViewById(R.id.progressBar);
        no_story=findViewById(R.id.no_stories);
        no_doubt=findViewById(R.id.no_doubts);
        no_coins=findViewById(R.id.no_coins);
        back_btn = findViewById(R.id.back_btn);
        first_name_edt = findViewById(R.id.first_name_edt);
        last_name_edt = findViewById(R.id.last_name_edt);
        email_edt = findViewById(R.id.email_edt);
        phone_edt = findViewById(R.id.pass_edt);
        bio_edt=findViewById(R.id.bio_edt);
        save_btn = findViewById(R.id.save_btn);
        drawable_anim_fname = findViewById(R.id.drawable_anim_fname);
        drawable_anim_lname = findViewById(R.id.drawable_anim_lname);
        drawable_anim_email = findViewById(R.id.drawable_anim_email);
        drawable_anim_pass = findViewById(R.id.drawable_anim_pass);
        textview_fname_error = findViewById(R.id.textview_fname_error);
        textview_lname_error = findViewById(R.id.textview_lname_error);
        textview_email_error = findViewById(R.id.textview_email_error);
        textview_pass_error = findViewById(R.id.textview_pass_error);
        name_txt = findViewById(R.id.name_txt);
        bio_txt=findViewById(R.id.bio_txt);
        upload_btn = findViewById(R.id.upload_btn);
        profile_image = findViewById(R.id.profile_image);
        edit_btn = findViewById(R.id.edit_btn);
        tvGood = findViewById(R.id.tvGood);
        uplaod_progressbar = findViewById(R.id.uplaod_progressbar);
        transparent = findViewById(R.id.transparent);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(UploadAPI.class);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            name_txt.setText(sharedPreferences.getString("fullname",""));
            first_name_edt.setText(sharedPreferences.getString("fullname",""));
            last_name_edt.setText(sharedPreferences.getString("stud_number",""));
            email_edt.setText(sharedPreferences.getString("email",""));
            phone_edt.setText(sharedPreferences.getString("password",""));
            user_id = sharedPreferences.getString("customer_id","");
            cust_img = sharedPreferences.getString("customer_image","");
            user_name = sharedPreferences.getString("username","");

            cust_id=Integer.parseInt(user_id);
            SharedPreferences sharedPreferences1 = getSharedPreferences("userdetails",MODE_PRIVATE);
            String url = sharedPreferences1.getString("customer_image","https://img.icons8.com/officel/2x/user.png");
            Glide.with(getApplicationContext()).load(url).into(profile_image);
//            Picasso.get().load(img_base_url+url.trim()).into(profile_image);

            //Get the time of day
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);

            //Set greeting
            String greeting = null;
            if(hour>=6 && hour<12){
                greeting = "Good Morning,";
            } else if(hour>= 12 && hour < 17){
                greeting = "Good Afternoon,";
            } else if(hour >= 17 && hour < 24){
                greeting = "Good Evening,";
            }
            else if(hour >= 0 && hour < 4){
                greeting = "Good Night,";
            }
//        else if(hour >= 21 && hour < 24){
//            greeting = "Good Night";
//        }

            tvGood.setText(greeting);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void listeners(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name_edt.setEnabled(true);
                last_name_edt.setEnabled(true);
                //email_edt.setEnabled(true);
                phone_edt.setEnabled(true);
                bio_edt.setEnabled(true);
                show = true;
                save_btn.animate().alpha(1.0f).translationY(-1.0f);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                first_name_edt.setFocusable(true);
                first_name_edt.setFocusableInTouchMode(true);
                first_name_edt.requestFocus();
            }
        });

        first_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fname = first_name_edt.getText().toString().trim();
                if(fname.equals("")){
                    textview_fname_error.setVisibility(View.VISIBLE);
                    textview_pass_error.setVisibility(View.GONE);
                    textview_lname_error.setVisibility(View.GONE);
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_fname.setAnimation("error.json");
                    drawable_anim_fname.playAnimation();
                }
                else{
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_fname.setVisibility(View.GONE);
                }
            }
        });

        last_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String lname = last_name_edt.getText().toString().trim();
                if(lname.equals("")){
                    textview_lname_error.setVisibility(View.VISIBLE);
                    textview_pass_error.setVisibility(View.GONE);
                    textview_fname_error.setVisibility(View.GONE);
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_lname.setAnimation("error.json");
                    drawable_anim_lname.playAnimation();
                }
                else{
                    textview_lname_error.setVisibility(View.GONE);
                    drawable_anim_lname.pauseAnimation();
                }
            }
        });

        email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = email_edt.getText().toString().trim();
                if(email.equals("")){
                    textview_email_error.setVisibility(View.VISIBLE);
                    textview_lname_error.setVisibility(View.GONE);
                    textview_pass_error.setVisibility(View.GONE);
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_email.setAnimation("error.json");
                    drawable_anim_email.playAnimation();
                }
                else{
                    textview_email_error.setVisibility(View.GONE);
                    drawable_anim_email.setVisibility(View.GONE);
                }
            }
        });

        phone_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = phone_edt.getText().toString().trim();
                if(phone.equals("")){
                    textview_email_error.setVisibility(View.GONE);
                    textview_lname_error.setVisibility(View.GONE);
                    textview_pass_error.setVisibility(View.VISIBLE);
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_pass.setAnimation("error.json");
                    drawable_anim_pass.playAnimation();
                }else if(phone.length()<6){
                    textview_pass_error.setVisibility(View.VISIBLE);
                    textview_pass_error.setText("Password should be at least 6 digit");
                    drawable_anim_pass.setAnimation("error.json");
                    drawable_anim_pass.playAnimation();
                }
                else{
                    textview_pass_error.setVisibility(View.GONE);
                    drawable_anim_pass.setVisibility(View.GONE);
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(NewProfileAct.this, "userID:"+user_id+"&custID:"+cust_id, Toast.LENGTH_SHORT).show();
                if(textview_fname_error.getVisibility() == View.GONE && textview_lname_error.getVisibility() == View.GONE && textview_email_error.getVisibility() == View.GONE
                        && textview_pass_error.getVisibility() == View.GONE){
                    String fullname = first_name_edt.getText().toString().trim();
                    String stud_number = last_name_edt.getText().toString().trim();
                    String email = email_edt.getText().toString().trim();
                    String password = phone_edt.getText().toString();
                    String bios = bio_edt.getText().toString();

                    first_name_edt.setEnabled(false);
                    last_name_edt.setEnabled(false);
                    email_edt.setEnabled(false);
                    phone_edt.setEnabled(false);
                    bio_edt.setEnabled(false);


                    profileViewModel.update(user_id,fullname,email,stud_number,password,cust_img,user_name,bios).observe(NewProfileAct.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if(s.equals("Update successfull")){
                                Toast.makeText(getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT).show();
                                v.animate().alpha(0.0f).translationY(0.0f);
                                first_name_edt.setFocusable(false);
                            }else
                                Toast.makeText(NewProfileAct.this, "Failed to update!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    showAll(v);
                }else{
                    requestPermissions();
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);
        if(resultCode != 0){
            profile_image.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkPermissions() {


        int permissionState3 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int permissionState5 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return
                permissionState3 == PackageManager.PERMISSION_GRANTED &&
                        permissionState5 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermissions() {

        boolean shouldProvideRationale3 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);

        boolean shouldProvideRationale5 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale3 || shouldProvideRationale5) {
            ActivityCompat.requestPermissions(NewProfileAct.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(NewProfileAct.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }


    }


    public void showAll(View view) {
        refreshImagePicker();
        //imagePicker.choosePicture(true);
        imagePicker.getImageFile();
        //r_photo.setVisibility(View.VISIBLE);
    }

    private void refreshImagePicker() {
        imagePicker = new ImagePicker(this,
                null,
                imageUri -> {
                    profile_image.setImageURI(imageUri);
                    try {
                        file = new File (FileUtils.getPath(imageUri,getApplicationContext()));
                        compressedImageFile = new Compressor(this).compressToFile(file);
                        uploadImageToServer();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    String doubts,stories,coins, bio;
    private void getPostData(){

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST +"stories_doubts_count", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray courses = jsonObject.getJSONArray("posts_data");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.d("respo posts",response);
                if (status == 1) {
                    try{
                        Log.i("respo is at 0",""+courses.getJSONObject(0));

                        doubts=courses.getJSONObject(0).getString("no_post");
                        stories=courses.getJSONObject(1).getString("no_stories");
                        coins=courses.getJSONObject(2).getString("coins");
                        bio=courses.getJSONObject(3).getString("bio");

                        Log.i("respo is at 1",""+courses.getJSONObject(1));

                    }
                    catch (Exception e){
                        Log.e("Prof Act",e.getMessage());
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
                parameters.put("customer_id",user_id);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
//                progressBar.setVisibility(View.GONE);
                no_doubt.setText(doubts);
                no_story.setText(stories);
                no_coins.setText(" "+coins);
                bio_edt.setText(bio);
                bio_txt.setText(bio);
            }
        });
    }

    private void uploadImageToServer() {

        transparent.setVisibility(View.VISIBLE);
        uplaod_progressbar.setVisibility(View.VISIBLE);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("img1", compressedImageFile.getName(), requestFile);

        apiRequest.ProfilePicUpload(cust_id,body)
                .enqueue(new Callback<ImageModel>() {

                    @Override
                    public void onResponse(Call<ImageModel> call, retrofit2.Response<ImageModel> response) {

                        Log.d(TAG, "onResponse: "+response.body());

                        Log.d(TAG, "Image URL: "+response.body().getResult().getUrlImg());

                        img_url = ""+response.body().getResult().getUrlImg();

                        if(!img_url.equals("")){
                            //Toast.makeText(getApplicationContext(),"Profile photo uploaded",Toast.LENGTH_SHORT).show();
                            transparent.setVisibility(View.GONE);
                            uplaod_progressbar.setVisibility(View.GONE);
                        }

                        SharedPref sharedPref = new SharedPref();

                        sharedPref.setProfileImage(getApplicationContext(),response.body().getResult().getUrlImg());
                    }

                    @Override
                    public void onFailure(Call<ImageModel> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}