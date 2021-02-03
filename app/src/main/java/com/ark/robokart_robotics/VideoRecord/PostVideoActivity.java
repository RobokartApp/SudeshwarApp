package com.ark.robokart_robotics.VideoRecord;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PostVideoActivity extends AppCompatActivity implements ServiceCallback {

    ImageView thumbnail,back_btn;
    Bitmap bmThumbnail;
    String video_path,desc,user_id,TAG;
    Button post_btn;
    static ProgressBar uplaod_progressbar;
    EditText vid_desc;

    int cust_id;
    private static final String SERVER_PATH = ApiConstants.image_HOST;
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    int status=0;
    ServiceCallback serviceCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","");
        cust_id=Integer.parseInt(user_id);

        TAG="post video Act";

        thumbnail=findViewById(R.id.thumbnail_vid);
        back_btn=findViewById(R.id.back_btn);
        post_btn=findViewById(R.id.btn_Post);
        uplaod_progressbar=findViewById(R.id.uplaod_progressbar);
        vid_desc=findViewById(R.id.vid_desc);

        video_path = Variables.outputfile2;

        Log.d("Video path ",video_path);

        bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_path,
                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

        thumbnail.setImageBitmap(bmThumbnail);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uplaod_progressbar.setVisibility(View.VISIBLE);
                uploadVideoToServer();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostVideoActivity.super.onBackPressed();
            }
        });
    }

    private void uploadVideoToServer() {

        uplaod_progressbar.setVisibility(View.VISIBLE);
        desc=vid_desc.getText().toString();

        File vidFile=new File(video_path);
        Log.d("file at uploading",""+vidFile);
        Uri uri=Uri.fromFile(vidFile);
Random random=new Random();
int rn=random.nextInt(9999);
        uploadPDF("vid"+rn+vidFile.getName(),uri,desc);
        //uploadVideo(vidFile.getName(),Uri.fromFile(vidFile));

        //Call<ResultObject>  serverCom = vInterface.uploadVideoToServer();

    }

    private void uploadPDF(final String pdfname, Uri pdffile,final String Description){
/*
        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);
            final ProgressDialog pDialog = ProgressDialog.show(PostVideoActivity.this,"","Uploading...",false);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiConstants.image_HOST+ApiConstants.post_video_api,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            pDialog.dismiss();
                            Log.d("ressssssoo",new String(response.data));
                            //Toast.makeText(PostVideoActivity.this, ""+new String(response.data), Toast.LENGTH_SHORT).show();
                           if(new String(response.data).contains("\"statusId\":1")){
                               Log.i("status id","true");
                               finish();
                               Intent intent=new Intent(PostVideoActivity.this,HomeActivity.class);
                               intent.putExtra("post","story");
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                           }else
                               Toast.makeText(PostVideoActivity.this, "Failed to Upload video!", Toast.LENGTH_SHORT).show();
                            rQueue.getCache().clear();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "errr"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("User_id", String.valueOf(user_id));
                    params.put("post_desc",Description);

                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("filename", new DataPart(pdfname ,inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)


            );

            volleyMultipartRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            rQueue = Volley.newRequestQueue(PostVideoActivity.this);
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
Start_Service();

    }
    public void Start_Service(){

        serviceCallback=this;

        Upload_Service mService = new Upload_Service(serviceCallback);
        if (!Functions.isMyServiceRunning(this,mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());

            mServiceIntent.setAction("startservice");
            mServiceIntent.putExtra("uri",""+ video_path);
            mServiceIntent.putExtra("user_id",""+ user_id);
            mServiceIntent.putExtra("vid_desc",""+ vid_desc.getText().toString());
        /*    mServiceIntent.putExtra("draft_file",draft_file);
            mServiceIntent.putExtra("duet_video_id",duet_video_id);

            mServiceIntent.putExtra("desc",""+description_edit.getText().toString());
            mServiceIntent.putExtra("privacy_type",privcy_type_txt.getText().toString());
*/

            startService(mServiceIntent);

/*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent("uploadVideo"));
                    Intent intent=new Intent(PostVideoActivity.this, HomeActivity.class);
                    intent.putExtra("post","story");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            },1000);
*/

        }
        else {
            Toast.makeText(this, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void ShowResponce(final String responce) {

        if(mConnection!=null)
            unbindService(mConnection);

        Toast.makeText(this, responce, Toast.LENGTH_SHORT).show();
    }
    Upload_Service mService;
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Upload_Service.LocalBinder binder = (Upload_Service.LocalBinder) service;
            mService = binder.getService();

            mService.setCallbacks(PostVideoActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };



}