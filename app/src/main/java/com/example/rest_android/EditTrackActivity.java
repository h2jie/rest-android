package com.example.rest_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTrackActivity extends AppCompatActivity {
    private EditText titleEditText, singerEditText;
    private Button saveButton, deleteButton;
    private String trackId;
    private TrackApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_track);

        initViews();
        getIntentData();
        setupListeners();
    }

    private void initViews() {
        titleEditText = findViewById(R.id.titleET);
        singerEditText = findViewById(R.id.singerET);
        saveButton = findViewById(R.id.saveBtn);
        deleteButton = findViewById(R.id.deleteBtn);
        apiService = TrackService.getApi();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            trackId = intent.getStringExtra("track_id");
            String title = intent.getStringExtra("track_title");
            String singer = intent.getStringExtra("track_singer");

            titleEditText.setText(title);
            singerEditText.setText(singer);
        }
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> updateTrack());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void updateTrack() {
        String title = titleEditText.getText().toString().trim();
        String singer = singerEditText.getText().toString().trim();

        if (title.isEmpty() || singer.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Track track = new Track(trackId, title, singer);  // id设为null，因为它会在URL中传递

        Log.d("EditTrackActivity", "Updating track with ID: " + trackId);
        Log.d("EditTrackActivity", "Title: " + title);
        Log.d("EditTrackActivity", "Singer: " + singer);

        apiService.updateTrack(track).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("EditTrackActivity", "Response code: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(EditTrackActivity.this, "Track updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e("EditTrackActivity", "Error body: " + errorBody);
                            Toast.makeText(EditTrackActivity.this,
                                    "Failed to update track: " + errorBody, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditTrackActivity.this,
                                "Error processing response", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EditTrackActivity", "Network error: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(EditTrackActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Track")
                .setMessage("Are you sure you want to delete this track?")
                .setPositiveButton("Delete", (dialog, which) -> deleteTrack())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTrack() {
        apiService.deleteTrack(trackId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditTrackActivity.this, "Track deleted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditTrackActivity.this, "Failed to delete track", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditTrackActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}