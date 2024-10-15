public class TrackApiService {
    @GET("/tracks")
    Call<List<Track>> getTracks();  // 获取所有 Track

    @POST("/tracks")
    Call<Track> addTrack(@Body Track track);  // 添加新的 Track

    @DELETE("/tracks/{id}")
    Call<Void> deleteTrack(@Path("id") String id);  // 删除 Track
}
}
