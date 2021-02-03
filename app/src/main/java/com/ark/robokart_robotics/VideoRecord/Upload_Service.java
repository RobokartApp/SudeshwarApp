package com.ark.robokart_robotics.VideoRecord;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ark.robokart_robotics.Activities.Home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

// this the background service which will upload the video into database
public class Upload_Service extends Service{

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public Upload_Service getService() {
            return Upload_Service.this;
        }
    }

    boolean mAllowRebind;
    ServiceCallback Callback;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    String draft_file,duet_video_id;
    String videopath;
    String description;
    String user_id;
    String allow_comment,allow_duet;
    SharedPreferences sharedPreferences;

    public Upload_Service() {
        super();
    }

    public Upload_Service(ServiceCallback serviceCallback) {
        Callback=serviceCallback;
       }

    public void setCallbacks(ServiceCallback serviceCallback){
        Callback=serviceCallback;
    }


    @Override
    public void onCreate() {
        sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!=null){
        if (intent.getAction().equals("startservice")) {
            showNotification();

            videopath =intent.getStringExtra("uri");

            Log.i("Upload Service","vid path:"+videopath);

            description=intent.getStringExtra("vid_desc");
            user_id=intent.getStringExtra("user_id");
    /*        draft_file=intent.getStringExtra("draft_file");
            duet_video_id=intent.getStringExtra("duet_video_id");

            privacy_type=intent.getStringExtra("privacy_type");
            allow_comment=intent.getStringExtra("allow_comment");
            allow_duet=intent.getStringExtra("allow_duet");
*/
            new Thread(new Runnable() {
                @Override
                public void run() {

                    MultiPartRequest request=new MultiPartRequest(Upload_Service.this, new Callback() {
                        @Override
                        public void Responce(String resp) {

                            Log.d("api resp"," "+resp);

                            try {
                                JSONObject jsonObject=new JSONObject(resp);
                                String code=jsonObject.optString("statusId");
                                //new String(response.data).contains("\"statusId\":1")
                                if(code.equalsIgnoreCase("1")){

                                    //Variables.Reload_my_videos=true;
                                    //Variables.Reload_my_videos_inner=true;
                                    //Delete_draft_file();

                                    Toast.makeText(Upload_Service.this, "Your Video is uploaded Successfully", Toast.LENGTH_LONG).show();


                                            //sendBroadcast(new Intent("uploadVideo"));
                                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                            intent.putExtra("post","story");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                }else{
                                    Toast.makeText(Upload_Service.this, "Your Video is failed to upload!", Toast.LENGTH_SHORT).show();
                                    //Log.d("not 200",""+code+" "+resp);
                                    Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("post","story");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("Upload Service",e.getMessage());
                                Toast.makeText(Upload_Service.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("post","story");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

//                            stopForeground(true);
                            stopSelf();
                PostVideoActivity.uplaod_progressbar.setVisibility(View.GONE);

                            sendBroadcast(new Intent("uploadVideo"));
                            sendBroadcast(new Intent("newVideo"));


                        }
                    });
                /*    request.addString("fb_id", Variables.user_id);
                    request.addString("sound_id", Variables.Selected_sound_id);
                    request.addString("description", description);
                    request.addString("privacy_type", privacy_type);
                    request.addString("allow_comments",allow_comment);
                    request.addString("allow_duet",allow_duet);
                    params.put("User_id", String.valueOf(user_id));
                    params.put("post_desc",Description);
*/
                    request.addString("User_id", user_id);
                    request.addString("post_desc",description);
                    request.addvideoFile("filename", videopath,"RBK.mp4");
                    request.execute();


                }
            }).start();


        }


        else if(intent.getAction().equals("stopservice")){
            stopForeground(true);
            stopSelf();
           }

        }

        return Service.START_STICKY;
    }


    // this will show the sticky notification during uploading video
    private void showNotification() {

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        final String CHANNEL_ID = "default";
        final String CHANNEL_NAME = "Default";

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(defaultChannel);
        }

        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Video")
                .setContentText("Please wait! Video is uploading....")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        android.R.drawable.stat_sys_upload))
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        startForeground(101, notification);
    }





}