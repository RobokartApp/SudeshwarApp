package com.ark.robokart_robotics.ImageUpload;

import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.ImageModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadVideo {

    @Multipart
    @POST(ApiConstants.post_video_api)
    Call<ImageModel> VidUpload(@Part("User_id") int User_id,
                                      @Part("post_desc") String desc, @Part MultipartBody.Part video);

}
