package com.ark.robokart_robotics.VideoRecord;

import android.content.SharedPreferences;
import android.os.Environment;

public class Variables {

    public static final String SelectedAudio_AAC ="SelectedAudio.aac";

    public static final String root= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString();
    public static final String app_hided_folder =root+"/Robokart/";



    public static int max_recording_duration=60000;
    public static int recording_duration=60000;
    public static int min_time_recording=1000;

    public static String output_frontcamera= app_hided_folder + "output_frontcamera.mp4";
    public static String outputfile= app_hided_folder + "output.mp4";
    public static String outputfile2= app_hided_folder + "Rbk.mp4";
    public static String output_filter_file= app_hided_folder + "output-filtered.mp4";
    public static String gallery_trimed_video= app_hided_folder + "gallery_trimed_video.mp4";
    public static String gallery_resize_video= app_hided_folder + "gallery_resize_video.mp4";



    public static SharedPreferences sharedPreferences;
    public static final String pref_name="pref_name";

    public static String user_id;


    public static String tag="rbk_";

    public static String Selected_sound_id="null";


    public final static int Pick_video_from_gallery=791;

    public static final  String main_domain="https://domain.com/";
    public static final String base_url=main_domain+"API/";
    public static final String api_domain =base_url+"index.php?p=";

}
