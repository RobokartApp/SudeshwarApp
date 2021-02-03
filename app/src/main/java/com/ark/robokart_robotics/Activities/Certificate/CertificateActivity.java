package com.ark.robokart_robotics.Activities.Certificate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ark.robokart_robotics.R;

import java.io.File;
import java.io.FileNotFoundException;

import carbon.widget.Button;

import static android.content.ContentValues.TAG;

public class CertificateActivity extends AppCompatActivity {

    TextView cert_title;
    Button download;
    ImageView back_btn;
    String cust_name,cust_id,result_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        init();

        listener();

        isStoragePermissionGranted();

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
        cust_name = sharedPreferences.getString("fullname","Robokart");
        cust_id = sharedPreferences.getString("customer_id","1");

        Intent intent = getIntent();
        String cert_course=intent.getStringExtra("cert_course");
        result_id=intent.getStringExtra("result_id");

        String title="Hey "+cust_name+",  your certificate for course "+cert_course+" is available to download.";
        cert_title.setText(title);

    }

    private void init() {
        cert_title=findViewById(R.id.cert_title);

        download=findViewById(R.id.cert_download_cbtn);

        back_btn=findViewById(R.id.back_btn);
    }

    private void listener() {

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CertificateActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();

                String url ="https://robokart.com/download_cert_api.php?cust_id="+cust_id+"&id="+result_id;

                File direct = new File(Environment.DIRECTORY_DOCUMENTS);

                if (!direct.exists()) {
                    direct.mkdirs();
                }


                DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);

                request
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, cust_name+"certificate.pdf");

                mgr.enqueue(request);

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CertificateActivity.super.onBackPressed();
            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(CertificateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

}