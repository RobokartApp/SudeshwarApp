package com.ark.robokart_robotics.VideoRecord;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ark.robokart_robotics.R;
import com.ark.robokart_robotics.SegmentProgress.ProgressBarListener;
import com.ark.robokart_robotics.SegmentProgress.SegmentedProgressBar;
import com.coremedia.iso.boxes.Container;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraProperties;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecordVideoActivity extends AppCompatActivity implements View.OnClickListener {

    CameraView cameraView;

    int number=0;

    ArrayList<String> videopaths=new ArrayList<>();

    ImageButton record_image;
    ImageButton done_btn;
    boolean is_recording=false;
    boolean is_flash_on=false;

    ImageButton flash_btn;
    SegmentedProgressBar video_progress;
    LinearLayout camera_options;
    ImageButton rotate_camera,cut_video_btn;


    int sec_passed=0;
    long time_in_milis=0;

    TextView countdown_timer_txt;
    boolean is_recording_timer_enable;
    int recording_time=3;


    TextView short_video_time_txt,long_video_time_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_record_a);

        Variables.Selected_sound_id="null";
        Variables.recording_duration=Variables.max_recording_duration;

        Functions.make_directry(Variables.app_hided_folder);

        cameraView = findViewById(R.id.camera);

        camera_options=findViewById(R.id.camera_options);

        record_image=findViewById(R.id.record_image);


        findViewById(R.id.upload_layout).setOnClickListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        cut_video_btn=findViewById(R.id.cut_video_btn);
        cut_video_btn.setVisibility(View.GONE);
        cut_video_btn.setOnClickListener(this);

        done_btn=findViewById(R.id.done);
        done_btn.setEnabled(false);
        done_btn.setOnClickListener(this);


        rotate_camera=findViewById(R.id.rotate_camera);
        rotate_camera.setOnClickListener(this);
        flash_btn=findViewById(R.id.flash_camera);
        flash_btn.setOnClickListener(this);

        findViewById(R.id.Goback).setOnClickListener(this);



        record_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start_or_Stop_Recording();
            }
        });
        countdown_timer_txt=findViewById(R.id.countdown_timer_txt);


        short_video_time_txt=findViewById(R.id.short_video_time_txt);
        long_video_time_txt=findViewById(R.id.long_video_time_txt);
        short_video_time_txt.setOnClickListener(this);
        long_video_time_txt.setOnClickListener(this);

        initlize_Video_progress();

    }


    public void initlize_Video_progress(){
        sec_passed=0;
        video_progress=findViewById(R.id.video_progress);
        video_progress.enableAutoProgressView(Variables.recording_duration);
        video_progress.setDividerColor(Color.WHITE);
        video_progress.setDividerEnabled(true);
        video_progress.setDividerWidth(4);
        video_progress.setShader(new int[]{Color.CYAN, Color.CYAN, Color.CYAN});

        video_progress.SetListener(new ProgressBarListener() {
            @Override
            public void TimeinMill(long mills) {
                time_in_milis=mills;
                sec_passed = (int) (mills/1000);

                if(sec_passed>(Variables.recording_duration/1000)-1){
                    Start_or_Stop_Recording();
                }

                if(is_recording_timer_enable && sec_passed>=recording_time){
                    is_recording_timer_enable=false;
                    Start_or_Stop_Recording();
                }

            }
        });
    }

    // if the Recording is stop then it we start the recording
    // and if the mobile is recording the video then it will stop the recording
    public void Start_or_Stop_Recording(){

        if (!is_recording && sec_passed<(Variables.recording_duration/1000)-1) {
            number=number+1;

            is_recording=true;

            File file = new File(Variables.app_hided_folder +  "myvideo"+(number)+".mp4");
            videopaths.add(Variables.app_hided_folder +  "myvideo"+(number)+".mp4");
            cameraView.setVideoQuality(CameraKit.Constants.VIDEO_QUALITY_480P);
            cameraView.setVideoBitRate(400000);

            cameraView.captureVideo(file);


            if(audio!=null)
                audio.start();


            done_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_done));
            done_btn.setEnabled(false);

            video_progress.resume();



            record_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_recoding_yes));

            cut_video_btn.setVisibility(View.GONE);


            findViewById(R.id.time_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.upload_layout).setEnabled(false);
            camera_options.setVisibility(View.GONE);
//            add_sound_txt.setClickable(false);
            rotate_camera.setVisibility(View.GONE);

        }

        else if (is_recording) {

            is_recording=false;

            video_progress.pause();
            video_progress.addDivider();

            if(audio!=null && audio.isPlaying())
                audio.pause();

            cameraView.stopVideo();


            Check_done_btn_enable();

            cut_video_btn.setVisibility(View.VISIBLE);

            findViewById(R.id.upload_layout).setEnabled(true);
            record_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_recoding_no));
            camera_options.setVisibility(View.VISIBLE);

        }

        else if(sec_passed>(Variables.recording_duration/1000)){
            Functions.Show_Alert(this,"Alert","Video only can be a "+ Variables.recording_duration /1000+" S");
        }

    }


    public void Check_done_btn_enable(){
        if(sec_passed>(Variables.min_time_recording/1000)) {
            done_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_red));
            done_btn.setEnabled(true);
        }else {
            done_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_done));
            done_btn.setEnabled(false);
        }
    }


    // this will apped all the videos parts in one  fullvideo
    private boolean append() {
        final ProgressDialog progressDialog=new ProgressDialog(RecordVideoActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    public void run() {

                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                    }
                });

                ArrayList<String> video_list=new ArrayList<>();
                for (int i=0;i<videopaths.size();i++){
Log.i("video path",videopaths.get(i));
                    File file=new File(videopaths.get(i));
                    if(file.exists()) {
                        try {

                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(RecordVideoActivity.this, Uri.fromFile(file));
                            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                            boolean isVideo = "yes".equals(hasVideo);

                            if (isVideo && file.length() > 3000) {
                                Log.d("resp", videopaths.get(i));
                                video_list.add(videopaths.get(i));
                            }
                        }catch (Exception e){
                            Log.d(Variables.tag,e.toString());
                        }
                    }
                }

                try {

                    Movie[] inMovies = new Movie[video_list.size()];

                    for (int i=0;i<video_list.size();i++){

                        inMovies[i]= MovieCreator.build(video_list.get(i));
                    }


                    List<Track> videoTracks = new LinkedList<Track>();
                    List<Track> audioTracks = new LinkedList<Track>();
                    for (Movie m : inMovies) {
                        for (Track t : m.getTracks()) {
                            if (t.getHandler().equals("soun")) {
                                audioTracks.add(t);
                            }
                            if (t.getHandler().equals("vide")) {
                                videoTracks.add(t);
                            }
                        }
                    }

                    Movie result = new Movie();
                    if (audioTracks.size() > 0) {
                        result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                    }
                    if (videoTracks.size() > 0) {
                        result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
                    }

                    Container out = new DefaultMp4Builder().build(result);

                    String outputFilePath=null;
                    if(cameraView.isFacingFront()){
                        outputFilePath=Variables.output_frontcamera;

                    }else {

                            outputFilePath = Variables.outputfile2;

                    }
                    FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
                    out.writeContainer(fos.getChannel());
                    fos.close();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            progressDialog.dismiss();

                            if(cameraView.isFacingFront()){
                               /* if(audio!=null)
                                    Change_fliped_video(Variables.output_frontcamera,Variables.outputfile);
                                else*/
                                    Change_fliped_video(Variables.output_frontcamera,Variables.outputfile2);
                            }
                            else {
                                    //Go_To_preview_Activity();
                                Uri uri = Uri.fromFile(new File(Variables.outputfile2));
                               // int file_size = Integer.parseInt(String.valueOf(new File(uri.getPath()).length()/1024));
                                //trimVid(uri.getPath(),Variables.gallery_trimed_video);
                                Chnage_Video_size(uri.getPath(),Variables.gallery_resize_video);
/*
                                if(file_size>2200) {
                                    try {
                                        Log.d("uri data", "" + uri);

                                        int[] wAndh = TrimmerUtils.getVideoWidthHeight(RecordVideoActivity.this, uri);

                                        int width = wAndh[0];
                                        int height = wAndh[1];
                                        if (wAndh[0] > 800) {
                                            width /= 2;
                                            width /= 2;
                                        }


                                        TrimVideo.activity(String.valueOf(uri))
                                                .setCompressOption(new CompressOption(30, "1M", width, height))
                                                .setHideSeekBar(true)
                                                .setTrimType(TrimType.MIN_MAX_DURATION)
                                                .setMinToMax(5, 60)
                                                .start(RecordVideoActivity.this);



                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    TrimVideo.activity(String.valueOf(uri))
                                            .setHideSeekBar(true)
                                            .setTrimType(TrimType.MIN_DURATION)
                                            .setMinDuration(5)
                                            .start(RecordVideoActivity.this);
                                }
*/
                            }

                        }
                    });



                }

                catch (Exception e) {
                }

            }
        }).start();

        return true;
    }

    public void Go_To_preview_Activity(){
        Intent intent =new Intent(this,PreviewVideoAct.class);
        //Toast.makeText(this, "Going for preview", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

    }

    public void Change_fliped_video(String srcMp4Path, final String destMp4Path){

        Functions.Show_determinent_loader(this,false,false);
        new GPUMp4Composer(srcMp4Path, destMp4Path)
                .flipHorizontal(true)
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d("resp",""+(int) (progress*100));
                        Functions.Show_loading_progress((int)(progress*100));
                    }

                    @Override
                    public void onCompleted() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();

                                  //  Uri uri = Uri.fromFile(new File(destMp4Path));
                                Go_To_preview_Activity();
                                    //int file_size = Integer.parseInt(String.valueOf(new File(uri.getPath()).length()/1024));
                                    /*
                                    if(file_size>2200) {
                                        try {
                                            Log.d("uri data", "" + uri);
                                            // Toast.makeText(this, "URI is: "+uri, Toast.LENGTH_SHORT).show();
                                            //startTrim(new File(uri.getPath()),new File(Variables.outputfile),0,18);

                                            //Intent intent=new Intent(this, TrimVideoAct.class);
                                            //intent.putExtra("path",""+ uri);

                                            //startActivity(intent);
                                            int[] wAndh = TrimmerUtils.getVideoWidthHeight(RecordVideoActivity.this, uri);

                                            int width = wAndh[0];
                                            int height = wAndh[1];
                                            if (wAndh[0] > 800) {
                                                width /= 2;
                                                width /= 2;
                                            }
                                            TrimVideo.activity(String.valueOf(uri))
                                                    .setCompressOption(new CompressOption(30, "1M", width, height))
                                                    .setHideSeekBar(true)
                                                    .start(RecordVideoActivity.this);



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        TrimVideo.activity(String.valueOf(uri))
                                                .setHideSeekBar(true)
                                                .start(RecordVideoActivity.this);
                                    }
*/

                            }
                        });
                    }

                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.d("resp",exception.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Functions.cancel_determinent_loader();

                                    Toast.makeText(RecordVideoActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                })
                .start();


    }



    public void RotateCamera(){
        cameraView.toggleFacing();

        if(cameraView.getFacing()== CameraKit.Constants.FACING_FRONT)
            cameraView.setScaleX(1);

        CameraProperties properties=cameraView.getCameraProperties();
        Log.d(Variables.tag,properties.verticalViewingAngle+"--"+properties.horizontalViewingAngle);

    }


    public void Remove_Last_Section(){

        if(videopaths.size()>0){
            File file=new File(videopaths.get(videopaths.size()-1));
            if(file.exists()) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(RecordVideoActivity.this, Uri.fromFile(file));
                String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time );
                boolean isVideo = "yes".equals(hasVideo);
                if (isVideo) {
                    time_in_milis=time_in_milis-timeInMillisec;
                    video_progress.removeDivider();
                    videopaths.remove(videopaths.size()-1);
                        video_progress.updateProgress(this.time_in_milis-500);
                    video_progress.back_countdown(timeInMillisec);
                    if(audio!=null) {
                        int audio_backtime = (int) (audio.getCurrentPosition()- timeInMillisec);
                        audio.seekTo(audio_backtime);
                    }

                    sec_passed = (int) (time_in_milis/1000);

                    Check_done_btn_enable();

                }
            }

            if(videopaths.isEmpty()){

                findViewById(R.id.time_layout).setVisibility(View.VISIBLE);
                cut_video_btn.setVisibility(View.GONE);
//                add_sound_txt.setClickable(true);
                rotate_camera.setVisibility(View.VISIBLE);

                initlize_Video_progress();

            }

            file.delete();
        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rotate_camera:
                RotateCamera();
                break;

            case R.id.upload_layout:
                Pick_video_from_gallery();

                break;

            case R.id.done:
                append();
                break;

            case R.id.cut_video_btn:

                Functions.Show_Alert(this, null, "Discard the last clip?", "DELETE", "CANCEL", new Callback() {
                    @Override
                    public void Responce(String resp) {
                        if(resp.equalsIgnoreCase("yes")){
                            Remove_Last_Section();
                        }
                    }
                });

                break;

            case R.id.flash_camera:

                if(is_flash_on){
                    is_flash_on=false;
                    cameraView.setFlash(0);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));

                }

                else {
                    is_flash_on=true;
                    cameraView.setFlash(CameraKit.Constants.FLASH_TORCH);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                }

                break;

            case R.id.Goback:
                onBackPressed();
                break;

        }


    }


    public void Pick_video_from_gallery(){
        Log.d("picking vid"," from gallery");
        Intent intent =new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, Variables.Pick_video_from_gallery);
    }
    public String getPath(Uri uri) {

        //String[] projection = { MediaStore.Video.Media.DATA };

            String[] projection = {MediaStore.Video.Media.DATA};

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
    private static final long  MEGABYTE = 1024L * 1024L;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.d("Activity result","code: "+resultCode+"data: "+data);

            if (requestCode == Variables.Pick_video_from_gallery) {
                Uri uri = data.getData();

                MediaPlayer mp = MediaPlayer.create(this, uri);
                long duration = mp.getDuration();
                duration= TimeUnit.MILLISECONDS.toSeconds(duration);

                String fileSize = null;
                Cursor cursor = getApplicationContext().getContentResolver()
                        .query(uri, null, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {

                        // get file size
                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        if (!cursor.isNull(sizeIndex)) {
                            fileSize = cursor.getString(sizeIndex);
                        }
                    }
                } finally {
                    cursor.close();
                }
                double size_kb = Integer.parseInt(fileSize) /1024;
                double size_mb = size_kb / 1024;
                Log.e("RecVidAct","dur:"+duration+" & size:"+size_mb);
                mp.release();
                Log.d("Gallery uri data", "" + uri+"\n"+getPathFromUri(RecordVideoActivity.this,uri));
                if (size_mb<101.0) {
                    if (duration<=300)
                        Chnage_Video_size(getPathFromUri(RecordVideoActivity.this, uri), Variables.outputfile2);
                    else
                        makeDialog("Video duration should be less than 5 Minutes. Kindly trim it and try again.");
                        //Toast.makeText(this, "Video duration should be less than 5 Minutes.", Toast.LENGTH_SHORT).show();
                }
                else
                    makeDialog("Video size should be less than 101 MB. Please compress it and try again.");
                    //Toast.makeText(this, "Video size should be less than 101 MB. Plz compress it!", Toast.LENGTH_SHORT).show();


                //Intent intent=new Intent(RecordVideoActivity.this,TrimVidActivity.class);
                //intent.putExtra("video_path",Util.getPath(RecordVideoActivity.this,uri));
                //intent.putExtra("video_path",getPathFromUri(RecordVideoActivity.this,uri));
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);

            /*    int file_size = Integer.parseInt(String.valueOf(new File(uri.getPath()).length()/1024));
                if(file_size>2200) {
                    try {

                        int[] wAndh = TrimmerUtils.getVideoWidthHeight(this, uri);

                        int width = wAndh[0];
                        int height = wAndh[1];
                        if (wAndh[0] > 800) {
                            width /= 2;
                            width /= 2;
                        }
                        TrimVideo.activity(String.valueOf(uri))
                                .setCompressOption(new CompressOption(30, "1M", width, height))
                                .setHideSeekBar(true)
                                .setTrimType(TrimType.MIN_MAX_DURATION)
                                .setMinToMax(5, 60)
                                .start(RecordVideoActivity.this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    TrimVideo.activity(String.valueOf(uri))
                            .setTrimType(TrimType.MIN_MAX_DURATION)
                            .setHideSeekBar(true)
                            .setMinToMax(5, 60)
                            .start(RecordVideoActivity.this);
                }
*/
            }
/*
            if (requestCode == TrimVideo.VIDEO_TRIMMER_REQ_CODE && data != null) {
                Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(data));
                Log.d("after video trim","ok");
                //String path= data.getStringExtra(TrimmerConstants.TRIMMED_VIDEO_PATH);
                Chnage_Video_size(uri.getPath(),Variables.gallery_resize_video);
            }
*/

        }else
            Log.d("resultAct","failed "+resultCode);

    }

    private void makeDialog(String msg) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                    }
                })
                .show();
    }

    public void Chnage_Video_size(String src_path,String destination_path){


        try {
            Log.d("srcpath is",src_path);
            Log.d("dest path is",destination_path);
            Functions.copyFile(new File(src_path),
                    new File(destination_path));

            //Intent intent=new Intent(RecordVideoActivity.this, PreviewVideoAct.class);
            //intent.putExtra("video_path",Variables.outputfile2);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //finish();
            //startActivity(intent);
            Go_To_preview_Activity();

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(Variables.tag,e.toString());
        }

    }

    // this will play the sound with the video when we select the audio
    MediaPlayer audio;


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }


    @Override
    protected void onDestroy() {
        Release_Resources();
        super.onDestroy();

    }



    public void Release_Resources(){
        try {

            if (audio != null) {
                audio.stop();
                audio.reset();
                audio.release();
            }
            cameraView.stop();

        }catch (Exception e){

        }
        DeleteFile();
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you Sure? if you Go back you can't undo this action")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Release_Resources();

                        finish();
                        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);

                    }
                }).show();


    }

    // this will delete all the video parts that is create during priviously created video
    public void DeleteFile(){

        File output = new File(Variables.outputfile);
        File output2 = new File(Variables.outputfile2);

        File gallery_trimed_video = new File(Variables.gallery_trimed_video);
        File gallery_resize_video = new File(Variables.gallery_resize_video);


        if(output.exists()){
            output.delete();
        }

        if(output2.exists()){

            output2.delete();
        }


        if(gallery_trimed_video.exists()){
            gallery_trimed_video.delete();
        }

        if(gallery_resize_video.exists()){
            gallery_resize_video.delete();
        }

        for (int i=0;i<=12;i++) {

            File file = new File(Variables.app_hided_folder + "myvideo" + (i) + ".mp4");
            if (file.exists()) {
                file.delete();
            }

        }


    }

    public String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.d("getpath","exxternal storage provider");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
           else if (isDownloadsDocument(uri)) {
Log.d("getpath","download provider");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                Log.d("getpath","media provider");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.d("getpath","media store");
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}