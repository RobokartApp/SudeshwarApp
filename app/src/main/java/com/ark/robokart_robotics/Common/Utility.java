package com.ark.robokart_robotics.Common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.ark.robokart_robotics.BuildConfig;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Utility {

    private static ProgressHUD progressDialog = null;

    public static void showProgressDialog(Context mContext) {
        try {
            if (progressDialog != null) {

                if (!progressDialog.isShowing())
                    progressDialog = progressDialog.show((Activity) mContext, "");

            } else {
                progressDialog = new ProgressHUD(mContext, R.style.ProgressHUD);
                progressDialog = progressDialog.show((Activity) mContext, "");

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null) stopProgressDialog();
        }
    }

    /*private static void showProgressDialog(Context ctx, String message) {
        try {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = progressDialog.show((Activity) ctx, message);
                }
            } else {
                progressDialog = new ProgressHUD(ctx, R.style.ProgressHUD);
                progressDialog = progressDialog.show((Activity) ctx, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null) stopProgressDialog();
        }
        int currentOrientation = ctx.getResources().getConfiguration().orientation;
        Activity activity = (Activity) ctx;
        *//**//*
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }*/

    public static void stopProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
            progressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //method used to show custom error dialogues
    public static void showErrorDialog(final Context context, String message) {
        if (context != null) {
            com.ark.robokart_robotics.Common.AlertDialog alertDialog = new com.ark.robokart_robotics.Common.AlertDialog();
            alertDialog.showDialog(context,"ERROR", "Something went wrong. Try again later!", 0, "");
        }

    }

    public static boolean isInternetOn(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    public static String convertDateToSpecifiedFormat(String dateString, String inputFormat, String outputFormat) {
        String formatedDateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(dateString);
            sdf = new SimpleDateFormat(outputFormat, Locale.US);
            formatedDateString = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDateString;
    }



    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public  static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getTodaysDateStrig(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        return date;
    }

    public static boolean compareDates(String fromDate, String toDate, boolean isFromDate, boolean isToDate){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();
        try {
            convertedDate = dateFormat.parse(fromDate);
            convertedDate2 = dateFormat.parse(toDate);
            if (convertedDate.after(convertedDate2)) {
                return false;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    public static  boolean is18YearOld(int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year,month,day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -16);
        if (minAdultAge.before(userAge)) {
            return  false;
        }

        return true;
    }



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Context context) {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                switch (type) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("on getPath", "Exception", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static void openGoogleMap(String currentLat, String currentLong, String destinationLat, String destinationLong, Context mContext){

        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/"+currentLat+","+currentLong+"/"+destinationLat+","+destinationLong+"/data=!3m1!4b1");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mContext.startActivity(mapIntent);

    }

    public static void callingFunction(Context mContext, String contactNo){
        String phone = contactNo;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        mContext.startActivity(intent);

    }

    // private static  ProgressDialog p;
    private static Bitmap bitmap;

    public static File getImage(final Context context, final String url, final String name){
        Utility.showProgressDialog(context);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                try {
                    bitmap= Glide.
                            with(context).
                            asBitmap().
                            load(url).
                            into(100, 100).
                            get();
                } catch (final ExecutionException e) {

                } catch (final InterruptedException e) {

                }
                return null;
            }

            @SuppressLint("WrongThread")
            @Override
            protected void onPostExecute(Void aVoid) {

                Utility.stopProgressDialog();
                if(bitmap !=null){
                    File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                    try {
                        FileOutputStream out = new FileOutputStream(outputFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outputFile));
                    context.sendBroadcast(intent);

                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }.execute();

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
    }



    public static void openFile(File url, Context context) {

        try {

            Uri uri;

            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    //private static  ProgressDialog progressAddress;

    public static File getAddressProf(final Context context, final String url, final String name){
        //progressAddress = new ProgressDialog(context);
        Utility.showProgressDialog(context);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                try {
                    bitmap= Glide.
                            with(context).
                            asBitmap().
                            load(url).
                            into(100, 100).
                            get();
                } catch (final ExecutionException e) {

                } catch (final InterruptedException e) {

                }
                return null;
            }

            @SuppressLint("WrongThread")
            @Override
            protected void onPostExecute(Void aVoid) {
                //progressAddress.dismiss();
                Utility.stopProgressDialog();
                if(bitmap !=null){

                    File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                    try {
                        FileOutputStream out = new FileOutputStream(outputFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outputFile));
                    context.sendBroadcast(intent);

                }
            }

            @Override
            protected void onPreExecute() {
        /*progressAddress.setMessage(context.getResources().getString(R.string.saving));
        progressAddress.setIndeterminate(false);
        progressAddress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressAddress.setCancelable(false);
        progressAddress.show();*/
                super.onPreExecute();
            }
        }.execute();

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
    }

    //public static ProgressDialog crbProgress;

    public static File getCRBProof(final Context context, final String url, final String name){
        //crbProgress = new ProgressDialog(context);
        Utility.showProgressDialog(context);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                try {
                    bitmap= Glide.
                            with(context).
                            asBitmap().
                            load(url).
                            into(100, 100).
                            get();
                } catch (final ExecutionException e) {

                } catch (final InterruptedException e) {

                }
                return null;
            }

            @SuppressLint("WrongThread")
            @Override
            protected void onPostExecute(Void aVoid) {
                //crbProgress.dismiss();
                Utility.stopProgressDialog();
                if(bitmap !=null){
                    File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                    try {
                        FileOutputStream out = new FileOutputStream(outputFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outputFile));
                    context.sendBroadcast(intent);


                }
            }

            @Override
            protected void onPreExecute() {
        /*crbProgress.setMessage(context.getResources().getString(R.string.saving));
        crbProgress.setIndeterminate(false);
        crbProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        crbProgress.setCancelable(false);
        crbProgress.show();*/
                super.onPreExecute();
            }
        }.execute();

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
    }

    //public static ProgressDialog progressLicence;

    public static File getLicenceProof(final Context context, final String url, final String name){
        //progressLicence = new ProgressDialog(context);
        Utility.showProgressDialog(context);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                try {
                    bitmap= Glide.
                            with(context).
                            asBitmap().
                            load(url).
                            into(100, 100).
                            get();
                } catch (final ExecutionException e) {

                } catch (final InterruptedException e) {

                }
                return null;
            }

            @SuppressLint("WrongThread")
            @Override
            protected void onPostExecute(Void aVoid) {
                //progressLicence.dismiss();
                Utility.stopProgressDialog();
                if(bitmap !=null){
                    File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                    try {
                        FileOutputStream out = new FileOutputStream(outputFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outputFile));
                    context.sendBroadcast(intent);

                }
            }

            @Override
            protected void onPreExecute() {
        /*progressLicence.setMessage(context.getResources().getString(R.string.saving));
        progressLicence.setIndeterminate(false);
        progressLicence.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressLicence.setCancelable(false);
        progressLicence.show();*/
                super.onPreExecute();
            }
        }.execute();

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
    }


    public static int convertoPx(Context context, int dpvalue) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpvalue,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static int convertoDp(Context context, int pxvalue) {
        Resources r = context.getResources();
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pxvalue,
                r.getDisplayMetrics()
        );
        return dp;
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public  static File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                /*Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");*/
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Uri GetOutPutUri(File file, Context context){


        // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
        return FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file);

    }

    /*public static String getDistance(LatLng latlngA, LatLng latlngB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latlngA.latitude);
        locationA.setLongitude(latlngA.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latlngB.latitude);
        locationB.setLongitude(latlngB.longitude);

        float distance = locationA.distanceTo(locationB)/1000;//To convert Meter in Kilometer
        return String.format("%.2f", distance);
    }*/

    public static String getDistance(float lat_a, float lng_a, float lat_b, float lng_b) {
        // earth radius is in mile
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(lat_a))
                * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2)
                * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        double kmConvertion = 1.6093;
        // return new Float(distance * meterConversion).floatValue();
        return String.format("%.2f", convertKmsToMiles((float) (distance * kmConvertion))) + " miles away";
        // return String.format("%.2f", distance)+" m";
    }

    String distance;

    public  static  float convertKmsToMiles(float kms){
        float miles = (float) (0.621371 * kms);

        return miles;
    }
}
