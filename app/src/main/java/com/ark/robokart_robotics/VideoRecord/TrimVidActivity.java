package com.ark.robokart_robotics.VideoRecord;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ark.robokart_robotics.R;

import java.io.File;
import java.io.IOException;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class TrimVidActivity extends AppCompatActivity {

    String path;
Uri uri;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim_vid);

        context=this;
        Intent intent=getIntent();
        if(intent.hasExtra("video_path")){
            path=intent.getStringExtra("video_path");
            //uri=Uri.parse(intent.getStringExtra("video_path"));
        }
        Log.d("path at trim",path);

        K4LVideoTrimmer videoTrimmer = findViewById(R.id.timeLine);
        if (videoTrimmer != null) {
            videoTrimmer.setVideoURI(Uri.parse(path));
        }

        assert videoTrimmer != null;
        //videoTrimmer.setDestinationPath(Variables.gallery_trimed_video);
        videoTrimmer.setMaxDuration(60);


        videoTrimmer.setOnTrimVideoListener(new OnTrimVideoListener() {
            @Override
            public void getResult(Uri uri) {
                Log.i("result of trim","uri:"+new File(uri.getPath()));
                    Chnage_Video_size(uri.getPath(),Variables.outputfile2);

              // doCompress(uri.getPath());
            }

            @Override
            public void cancelAction() {
                finish();
                Log.i("Cancel Trim","true");
            }
        });
    }

    private void doCompress(String inputVideoPath) {

        Log.d("doFileUpload ", inputVideoPath);

    }

    /*  public String getPath(Uri uri) {

          String[] projection = { MediaStore.Video.Media.DATA };

          //String[] projection = {MediaStore.Video.Media.DATA,MediaStore.Video.Media.SIZE};

          Cursor cursor = getContentResolver().query(uri,
                  projection, null, null, null);

          if (cursor != null) {
              // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
              // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
              int column_index = cursor
                      .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
              cursor.moveToFirst();
              return cursor.getString(column_index);
          } else
              return null;

      }
  */
    public void Chnage_Video_size(String src_path,String destination_path){

        try {
            Log.d("srcpath is",""+src_path);
            Log.d("dest path is",destination_path);
            Functions.copyFile(new File(src_path),
                    new File(destination_path));

            File file=new File(src_path);
            //if(file.exists())
              //  file.deleteOnExit();
                //file.delete();

            Intent intent=new Intent(TrimVidActivity.this, PostVideoActivity.class);
           // intent.putExtra("video_path",Variables.gallery_trimed_video);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(Variables.tag,e.toString());
        }
    }


}