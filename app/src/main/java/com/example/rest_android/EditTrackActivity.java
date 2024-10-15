package com.example.rest_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class EditTrackActivity extends AppCompatActivity {
    private EditText titleEditText, singerEditText;
    private Button saveButton;
    private String trackId;
    private TrackApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_track);

        titleEditText = findViewById(R.id.trackTitleEdit);
        singerEditText = findViewById(R.id.trackSingerEdit);
        saveButton = findViewById(R.id.saveTrackButton);

        trackId = getIntent().getStringExtra("track_id");
        apiService = RetrofitClient.getClient().create(TrackApiService.class);

        // TODO: 通过 Track ID 加载当前 Track 的数据，并显示在 EditText 中

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String singer = singerEditText.getText().toString();

            // TODO: 调用 API 更新 Track
            finish();
        });
    }
}
