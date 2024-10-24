package com.example.rest_android;
public class TrackService {
    private static TrackApiService apiService = null;

    public static TrackApiService getApi() {
        if (apiService == null) {
            apiService = RetrofitClient.getClient().create(TrackApiService.class);
        }
        return apiService;
    }
}