package com.ark.robokart_robotics.Activities.AskDoubt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Common.FileUtils;
import com.ark.robokart_robotics.ImageUpload.RetrofitRequest;
import com.ark.robokart_robotics.ImageUpload.UploadPost;
import com.ark.robokart_robotics.Model.ImageModel;
import com.ark.robokart_robotics.R;
import com.myhexaville.smartimagepicker.ImagePicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CreateDoubtAct extends AppCompatActivity {

    ImageView back_btn,doubt_img;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private ImagePicker imagePicker;
    File file;
    File compressedImageFile;
    Button post_btn;
    EditText post_desc;
    private ProgressBar uplaod_progressbar;
    String TAG="new doubt";
    String user_id;
    public static int cust_id;
    String desc;
    private UploadPost apiRequest;
    private String img_url;
    private Uri imageToUploadUri;
    ProgressBar progressBar;
    int dj;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doubt);


dj=0;
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        requestQueue = Volley.newRequestQueue(this);
        init();
        listener();
        requestPermissions();
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);

    }
*/
    private void init() {
        doubt_img=findViewById(R.id.doubt_img);
        back_btn=findViewById(R.id.iv_Back);
        post_btn=findViewById(R.id.btn_Post);
        post_desc=findViewById(R.id.post_description);
        uplaod_progressbar = findViewById(R.id.uplaod_progressbar);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(UploadPost.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","");

        cust_id=Integer.parseInt(user_id);
    }

    private void listener(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDoubtAct.super.onBackPressed();
            }
        });
        doubt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()) {
                    showAll(view);
                }else{
                    requestPermissions();
                }

            }
        });
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CreateDoubtAct.this, "post: "+post_desc.getText(), Toast.LENGTH_SHORT).show();
                if(post_desc.getText().toString().equals("")){
                    //Toast.makeText(CreateDoubtAct.this, "No text is there", Toast.LENGTH_SHORT).show();
                    post_desc.setError("Enter post description");
                    post_desc.requestFocus();
                }else{
                    if(dj==1)
                        uploadImageToServer();
                    else
                        sendTextPost(post_desc.getText().toString());
                }
                    //Toast.makeText(CreateDoubtAct.this, "Post: "+dj, Toast.LENGTH_SHORT).show();
                    //uploadImageToServer();

            }
        });
    }

    public void showAll(View view) {
        refreshImagePicker();
        //imagePicker.choosePicture(true);
        //imagePicker.getImageFile();
        //r_photo.setVisibility(View.VISIBLE);
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);

         */
        selectImage(CreateDoubtAct.this);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    //Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    //takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    //imageToUploadUri = Uri.fromFile(f);
                    //startActivityForResult(takePicture, 0);
                    ContentValues values= new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE,"new pic");
                    values.put(MediaStore.Images.Media.DESCRIPTION,"cam clicked pic");
                    imageToUploadUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
                    //intent.putExtra("crop", "true");
                    /*// indicate aspect of desired crop
                    intent.putExtra("aspectX", 2);
                    intent.putExtra("aspectY", 1);
                    // indicate output X and Y
                    intent.putExtra("outputX", 580);
                    intent.putExtra("outputY", 480);*/
                    if(checkPermissions()) {

                        //if (intent.resolveActivity(getPackageManager())!=null)
                            startActivityForResult(intent, 0);
                    }
                    else
                        requestPermissions();

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //if (pickPhoto.resolveActivity(getPackageManager())!=null)
                        startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            dj=1;
            imagePicker.handleActivityResult(resultCode,requestCode,data);
            switch (requestCode) {
                case 0:
                    //&& data != null
                    if (resultCode == RESULT_OK ) {
                        //Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        //doubt_img.setImageBitmap(selectedImage);
                        doubt_img.setImageURI(imageToUploadUri);
                        doubt_img.setPadding(10,10,10,10);
                        Log.d("cam",""+imageToUploadUri);

                        file = new File(Objects.requireNonNull(FileUtils.getPath(imageToUploadUri, getApplicationContext())));
                        try {
                            compressedImageFile = new Compressor(this).compressToFile(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                doubt_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                doubt_img.setPadding(10,10,10,10);
                                cursor.close();
                            }
                            file = new File(Objects.requireNonNull(FileUtils.getPath(selectedImage, getApplicationContext())));
                            try {
                                compressedImageFile = new Compressor(this).compressToFile(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void refreshImagePicker() {
        imagePicker = new ImagePicker(this,
                null,
                imageUri -> {
                    doubt_img.setImageURI(imageUri);
                    try {
                        file = new File(Objects.requireNonNull(FileUtils.getPath(imageUri, getApplicationContext())));
                        compressedImageFile = new Compressor(this).compressToFile(file);

                        doubt_img.setPadding(10,10,10,10);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private boolean checkPermissions() {


        int permissionState3 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int permissionState5 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionState6 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        return
                permissionState3 == PackageManager.PERMISSION_GRANTED &&
                        permissionState5 == PackageManager.PERMISSION_GRANTED &&
                permissionState6==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {

        boolean shouldProvideRationale3 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);

        boolean shouldProvideRationale5 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean shouldProvideRationale6 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);

        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale3 || shouldProvideRationale5 || shouldProvideRationale6) {
            ActivityCompat.requestPermissions(CreateDoubtAct.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(CreateDoubtAct.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }

    }

    private void uploadImageToServer() {
        progressBar.setVisibility(View.VISIBLE);
        //uplaod_progressbar.setVisibility(View.VISIBLE);
desc=post_desc.getText().toString();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("img1", compressedImageFile.getName(), requestFile);

        apiRequest.ProfilePicUpload(cust_id,desc,body)
                .enqueue(new Callback<ImageModel>() {

                    @Override
                    public void onResponse(@NotNull Call<ImageModel> call, retrofit2.@NotNull Response<ImageModel> response) {

                        Log.d(TAG, "onResponse: "+response.body());

                        Log.d(TAG, "Image URL: "+response.body().getResult().getUrlImg());

                        img_url = ""+response.body().getResult().getUrlImg();

                        if(!img_url.equals("")){
                            //Toast.makeText(getApplicationContext(),"Profile photo uploaded",Toast.LENGTH_SHORT).show();
                            //uplaod_progressbar.setVisibility(View.GONE);
                            Log.i("CreateDoubtAct","imgUrl:"+img_url);
                            progressBar.setVisibility(View.GONE);
                            finish();
                            Intent intent=new Intent(CreateDoubtAct.this,HomeActivity.class);
                            intent.putExtra("post","ok");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }


                    }

                    @Override
                    public void onFailure(@NotNull Call<ImageModel> call, @NotNull Throwable t) {
                        //Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void sendTextPost(String postDesc) {
        progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(this, "post: "+postDesc, Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.text_post_api, response -> {
            Log.d("Text Post respo ",response);
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
                parameters.put("post_desc",postDesc);
                parameters.put("user_id",user_id);
                return parameters;
            }
        };
        requestQueue.add(request).setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.setVisibility(View.GONE);
                finish();
                Intent intent=new Intent(CreateDoubtAct.this,HomeActivity.class);
                intent.putExtra("post","ok");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}