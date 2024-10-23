package com.example.rest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private List<Track> trackList = new ArrayList<>();
    private TrackApiService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getClient().create(TrackApiService.class);

        // Create adapter
        trackAdapter = new TrackAdapter(trackList,this);

        // Assign adapter to recyclerview
        recyclerView.setAdapter(trackAdapter);

        loadTracks();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditTrackActivity.class);
            intent.putExtra("track_adapter",(ArrayList<Track>) trackList); // 传递适配器
            startActivity(intent);
        });

        Button addTrackButton = findViewById(R.id.addTrackButton);
        addTrackButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTrackActivity.class);
            startActivity(intent);
            //trackAdapter.notifyDataSetChanged();
        });


        setupSwipeToDelete();
    }



    private void loadTracks() {
        apiService.getTracks().enqueue(new Callback<List<Track>>() {

            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    List<Track> tracks = response.body();
                    trackList.addAll(response.body());
                    trackAdapter.notifyDataSetChanged();

                } else {
                    Log.e("MainActivity", "Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.e("MainActivity", "Network Error: " + t.getMessage());
            }
        });
    }


    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Track track = trackList.get(position);

                // delete Track
                apiService.deleteTrack(track.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            trackList.remove(position);
                            trackAdapter.notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // infor error
                    }
                });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}
