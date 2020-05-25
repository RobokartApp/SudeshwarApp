package com.ark.robokart_robotics.Activities.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ark.robokart_robotics.Common.FileUtils;
import com.ark.robokart_robotics.Common.SharedPref;
import com.ark.robokart_robotics.ImageUpload.RetrofitRequest;
import com.ark.robokart_robotics.ImageUpload.UploadAPI;
import com.ark.robokart_robotics.Model.ImageModel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import carbon.widget.Button;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    ImageView back_btn;

    private EditText first_name_edt, last_name_edt, email_edt, phone_edt;

    private Button save_btn;

    private LottieAnimationView drawable_anim_fname, drawable_anim_lname, drawable_anim_email, drawable_anim_number;

    private TextView textview_fname_error, textview_lname_error, textview_email_error, textview_phone_error;

    String user_id;

    private ImageView upload_btn;

    private CircleImageView profile_image;

    public ProfileViewModel profileViewModel;

    private ImagePicker imagePicker;

    private carbon.widget.TextView tvGood;

    File file;

    Uri image_uri;

    private UploadAPI apiRequest;

    private ImageButton edit_btn;

    private boolean show;

    private String img_base_url = "https://robokart.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        listeners();

    }

    // Initialise UI
    public void init(){
        back_btn = findViewById(R.id.back_btn);
        first_name_edt = findViewById(R.id.first_name_edt);
        last_name_edt = findViewById(R.id.last_name_edt);
        email_edt = findViewById(R.id.email_edt);
        phone_edt = findViewById(R.id.phone_edt);
        save_btn = findViewById(R.id.save_btn);
        drawable_anim_fname = findViewById(R.id.drawable_anim_fname);
        drawable_anim_lname = findViewById(R.id.drawable_anim_lname);
        drawable_anim_email = findViewById(R.id.drawable_anim_email);
        drawable_anim_number = findViewById(R.id.drawable_anim_number);
        textview_fname_error = findViewById(R.id.textview_fname_error);
        textview_lname_error = findViewById(R.id.textview_lname_error);
        textview_email_error = findViewById(R.id.textview_email_error);
        textview_phone_error = findViewById(R.id.textview_phone_error);
        upload_btn = findViewById(R.id.upload_btn);
        profile_image = findViewById(R.id.profile_image);
        edit_btn = findViewById(R.id.edit_btn);
        tvGood = findViewById(R.id.tvGood);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(UploadAPI.class);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            first_name_edt.setText(sharedPreferences.getString("fullname",""));
            last_name_edt.setText(sharedPreferences.getString("stud_number",""));
            email_edt.setText(sharedPreferences.getString("email",""));
            phone_edt.setText(sharedPreferences.getString("parent_number",""));
            user_id = sharedPreferences.getString("customer_id","");

            SharedPreferences sharedPreferences1 = getSharedPreferences("URL",MODE_PRIVATE);
            String url = sharedPreferences1.getString("image_url","https://img.icons8.com/officel/2x/user.png");
            Glide.with(getApplicationContext()).load(Uri.parse(url).toString().trim()).disallowHardwareConfig().into(profile_image);
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
//        else if(hour >= 21 && hour < 24){
//            greeting = "Good Night";
//        }

            tvGood.setText(greeting);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void toggle() {
//        Transition transition = new CircularReveal;
//        transition.setDuration(600);
//        transition.addTarget(R.id.save_btn);
//
//
//        save_btn.setVisibility(show ? View.VISIBLE : View.GONE);
//    }

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

                show = true;
                save_btn.setVisibility(View.VISIBLE);
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
                    textview_phone_error.setVisibility(View.GONE);
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
                    textview_phone_error.setVisibility(View.GONE);
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
                    textview_phone_error.setVisibility(View.GONE);
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
                    textview_phone_error.setVisibility(View.VISIBLE);
                    textview_fname_error.setVisibility(View.GONE);
                    drawable_anim_number.setAnimation("error.json");
                    drawable_anim_number.playAnimation();
                }
                else{
                    textview_phone_error.setVisibility(View.GONE);
                    drawable_anim_number.setVisibility(View.GONE);
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textview_fname_error.getVisibility() == View.GONE && textview_lname_error.getVisibility() == View.GONE && textview_email_error.getVisibility() == View.GONE
                  && textview_phone_error.getVisibility() == View.GONE){
                   String fullname = first_name_edt.getText().toString().trim();
                   String stud_number = last_name_edt.getText().toString().trim();
                   String email = email_edt.getText().toString().trim();
                   String parent_number = phone_edt.getText().toString();

                   profileViewModel.update(user_id,fullname,email,stud_number,parent_number).observe(ProfileActivity.this, new Observer<String>() {
                       @Override
                       public void onChanged(String s) {
                           if(s.equals("Update successfull")){
                               Toast.makeText(getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT).show();
                               save_btn.setVisibility(View.GONE);
                               first_name_edt.setFocusable(false);
                           }
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
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }


    }


    public void showAll(View view) {
        refreshImagePicker();
        imagePicker.choosePicture(true);
        //r_photo.setVisibility(View.VISIBLE);
    }

    private void refreshImagePicker() {
        imagePicker = new ImagePicker(this,
                null,
                imageUri -> {
                    profile_image.setImageURI(imageUri);
                    try {

                        file = new File (FileUtils.getPath(imageUri,getApplicationContext()));
                        uploadImageToServer();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void uploadImageToServer() {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("img1", file.getName(), requestFile);

        apiRequest.ProfilePicUpload(user_id,body)
                .enqueue(new Callback<ImageModel>() {

                    @Override
                    public void onResponse(Call<ImageModel> call, retrofit2.Response<ImageModel> response) {

                        Log.d(TAG, "onResponse: "+response.body());

                        Log.d(TAG, "Image URL: "+response.body().getResult().getUrlImg());

                        String img_url = response.body().getResult().getUrlImg();

                        if(!img_url.equals("")){
                            Toast.makeText(getApplicationContext(),"Profile photo uploaded",Toast.LENGTH_SHORT).show();
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
