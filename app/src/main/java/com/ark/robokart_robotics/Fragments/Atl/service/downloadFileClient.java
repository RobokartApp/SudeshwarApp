package com.ark.robokart_robotics.Fragments.Atl.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface downloadFileClient {
    @Streaming
    @GET
    Call<ResponseBody>downloadFile(@Url String url);
}
