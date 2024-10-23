package com.example.rest_android;

import static com.example.rest_android.R.id.recyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTrackActivity extends AppCompatActivity {
    private EditText titleEditText, singerEditText, idEditText;
    private Button saveButton;
    private TrackApiService apiService;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        titleEditText = findViewById(R.id.trackTitleEdit);
        singerEditText = findViewById(R.id.trackSingerEdit);
        idEditText = findViewById(R.id.trackIdEdit);
        saveButton = findViewById(R.id.saveTrackButton);

        apiService = RetrofitClient.getClient().create(TrackApiService.class);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String singer = singerEditText.getText().toString();
            String id = idEditText.getText().toString();

            Track newTrack = new Track(id, title, singer);

            apiService.addTrack(newTrack).enqueue(new Callback<Track>() {

                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {

                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    // 处理错误
                }
            });
        });
    }
}
