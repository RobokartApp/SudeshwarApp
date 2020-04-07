package com.ark.robokart_robotics.ImageUpload;

import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.ImageModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPI {

    @Multipart
    @POST(ApiConstants.profile_img_api)
    Call<ImageModel> ProfilePicUpload(@Part("User_id") String User_id,
                                                       @Part MultipartBody.Part image);

}
