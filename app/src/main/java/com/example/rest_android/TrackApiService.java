package com.example.rest_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrackApiService {
    @GET("/tracks")
    Call<List<Track>> getTracks();  // 获取所有 Track

    @POST("/tracks")
    Call<Track> addTrack(@Body Track track);  // 添加新的 Track

    @DELETE("/tracks/{id}")
    Call<Void> deleteTrack(@Path("id") String id);  // 删除 Track
}