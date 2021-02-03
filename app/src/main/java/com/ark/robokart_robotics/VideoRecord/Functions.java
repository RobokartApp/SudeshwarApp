package com.ark.robokart_robotics.VideoRecord;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.ark.robokart_robotics.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Functions {


    public static void Show_Alert(Context context,String title,String Message){
       new  AlertDialog.Builder(context)
               .setTitle(title)
               .setMessage(Message)
               .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               }).show();
    }
    public static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    public static void Show_Alert(Context context, String title, String Message, String postivebtn, String negitivebtn, final Callback callback){

        new AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(Message)
                .setNegativeButton(negitivebtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callback.Responce("no");
                    }
                })
                .setPositiveButton(postivebtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                      callback.Responce("yes");

                    }
                }).show();
    }

    public static Dialog dialog;

    public static void make_directry(String path){
        File dir = new File(path);
       /* if(!dir.exists())
        {
            dir.mkdir();
        }*/

        File root = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Robokart");
        if (!root.exists()) {
            root.mkdirs();
        }
    }
  public static Dialog indeterminant_dialog;



    public static Dialog determinant_dialog;
   public static ProgressBar determinant_progress;

    public static void Show_determinent_loader(Context context, boolean outside_touch, boolean cancleable) {

        determinant_dialog = new Dialog(context);
        determinant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        determinant_dialog.setContentView(R.layout.item_determinant_progress_layout);
        determinant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.d_round_white_background));

        determinant_progress=determinant_dialog.findViewById(R.id.pbar);

        if(!outside_touch)
            determinant_dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            determinant_dialog.setCancelable(false);

        determinant_dialog.show();

    }

    public static void Show_loading_progress(int progress){
        if(determinant_progress!=null ){
            determinant_progress.setProgress(progress);

        }
    }


    public static void cancel_determinent_loader(){
        if(determinant_dialog!=null){
            determinant_progress=null;
             determinant_dialog.cancel();
        }
    }


    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                //destination.close();
            }
        }
    }


}
