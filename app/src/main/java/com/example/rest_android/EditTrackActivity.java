package com.example.rest_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTrackActivity extends AppCompatActivity {
    private EditText titleEditText, singerEditText;
    private Button saveButton, deleteButton;
    private String trackId;
    private List<Track> trackList;
    private TrackAdapter trackAdapter; // 添加适配器引用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_track);
        trackList = (ArrayList<Track>) getIntent().getSerializableExtra("track_list");
        trackAdapter = new TrackAdapter(trackList, this);
        titleEditText = findViewById(R.id.titleET);
        singerEditText = findViewById(R.id.singerET);
        saveButton = findViewById(R.id.saveBtn);
        deleteButton = findViewById(R.id.deleteBtn);

        trackId = getIntent().getStringExtra("track_id");
        //trackAdapter = (TrackAdapter) getIntent().getSerializableExtra("track_adapter"); // 获取适配器

        loadTrackById(trackId);

        saveButton.setOnClickListener(v -> updateTrack());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(trackId));
    }

    private void loadTrackById(String trackId) {
        TrackApiService apiService = RetrofitClient.getClient().create(TrackApiService.class);
        apiService.getTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Track track : response.body()) {
                        if (track.getId().equals(trackId)) {
                            titleEditText.setText(track.getTitle());
                            singerEditText.setText(track.getSinger());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                // 处理错误
            }
        });
    }

    private void updateTrack() {
        Track track = new Track(trackId, titleEditText.getText().toString(), singerEditText.getText().toString());
        TrackApiService apiService = RetrofitClient.getClient().create(TrackApiService.class);
        apiService.addTrack(track).enqueue(new Callback<Track>() { // 这里应该是 Callback<Track>
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 处理成功响应，例如更新适配器中的曲目
                    int position = findTrackPositionById(trackId);
                    if (position != -1) {
                        trackList.set(position, response.body()); // 更新适配器中的曲目
                        trackAdapter.notifyItemChanged(position); // 刷新适配器中的项
                    }
                    finish(); // 关闭编辑界面
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                // 处理错误
            }
        });
    }


    private void showDeleteConfirmationDialog(String trackId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Track")
                .setMessage("Are you sure you want to delete this track?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteTrack(trackId))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteTrack(String trackId) {
        TrackApiService apiService = RetrofitClient.getClient().create(TrackApiService.class);
        apiService.deleteTrack(trackId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 从适配器中删除曲目
                    int position = findTrackPositionById(trackId);
                    trackAdapter.removeTrack(position);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 处理错误
            }
        });
    }


    private int findTrackPositionById(String trackId) {
        for (int i = 0; i < trackAdapter.getItemCount(); i++) {
            Track track = trackAdapter.trackList.get(i); // 访问适配器的曲目列表
            if (track.getId().equals(trackId)) {
                return i; // 返回找到的曲目位置
            }
        }
        return -1; // 如果没有找到，返回 -1
    }
}
