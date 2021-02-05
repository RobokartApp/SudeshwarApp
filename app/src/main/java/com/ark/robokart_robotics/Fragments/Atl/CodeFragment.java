package com.ark.robokart_robotics.Fragments.Atl;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.Fragments.Atl.service.downloadFileClient;
import com.ark.robokart_robotics.R;
import com.artjimlop.altex.AltexImageDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

/*
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;
*/
//import bolts.TaskCompletionSource;
//import static com.facebook.FacebookSdk.getApplicationContext;

public class CodeFragment extends Fragment {


    public CodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code, container, false);
    }
    TextView fragText;int indx;
String code,title,id,fileId,webcontentlink;
    TextView download;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
download=view.findViewById(R.id.download_btn);
isStoragePermissionGranted();
        indx=Integer.parseInt(AtlChooseLevel.indx);
        code=AtlChooseLevel.code.get(indx-1);
        title=AtlChooseLevel.title.get(indx-1);
        String[] sArr=code.split("/");
        fileId=sArr[5];//"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
final String url="https://docs.google.com/a/google.com/uc?id="+fileId;

download.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
/*
        webcontentlink = "https://docs.google.com/a/google.com/uc?id="+fileId+"&export=download";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(code));
        startActivity(browserIntent);*/
//downloadFile(url);
       downloadFile(url);
        DownloadManager downloadManager = (DownloadManager)getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       // Toast.makeText(getActivity(), "Downloading Your File..., Check Notification Bar", Toast.LENGTH_SHORT).show();
        Long reference = downloadManager.enqueue(request);
    }
});
    }
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    // ---

    Future<File> downloading;
private void downloadFile(String url){
    Retrofit.Builder builder=new Retrofit.Builder()
            .baseUrl("https://robokart.com");
    Retrofit retrofit=builder.build();

    downloadFileClient fileClient=retrofit.create(downloadFileClient.class);
    Call<ResponseBody> call=fileClient.downloadFile(url);
    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Boolean success=writeResponseBodyToDisk(response.body());
            Toast.makeText(getActivity(), "File has been downloaded in DOWNLOAD directory", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(getActivity(), "Oops!"+t, Toast.LENGTH_SHORT).show();
        }
    });
}
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    title+".ino");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

}