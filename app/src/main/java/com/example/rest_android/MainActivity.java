package com.example.rest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private List<Track> trackList = new ArrayList<>();
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        loadTracks();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        Button addButton = findViewById(R.id.addTrackButton);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTrackActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        trackAdapter = new TrackAdapter(trackList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trackAdapter);


        new ItemTouchHelper(new SwipeToDeleteCallback()).attachToRecyclerView(recyclerView);
    }

    private void loadTracks() {
        TrackService.getApi().getTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    trackList.clear();
                    trackList.addAll(response.body());
                    trackAdapter.notifyDataSetChanged();
                } else {
                    showError("Failed to load tracks");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void deleteTrack(Track track, int position) {
        TrackService.getApi().deleteTrack(track.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    trackList.remove(position);
                    trackAdapter.notifyItemRemoved(position);
                    showMessage("Track deleted");
                } else {
                    showError("Failed to delete track");
                    trackAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showError("Network error while deleting");
                trackAdapter.notifyItemChanged(position);
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Track track = trackList.get(position);
            deleteTrack(track, position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTracks();
    }
}