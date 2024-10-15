package com.example.rest_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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
    private TrackApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 Retrofit 客户端
        apiService = RetrofitClient.getClient().create(TrackApiService.class);

        // 加载所有曲目
        loadTracks();

        trackAdapter = new TrackAdapter(trackList, track -> {
            // 点击曲目，编辑曲目
            Intent intent = new Intent(MainActivity.this, EditTrackActivity.class);
            intent.putExtra("track_id", track.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(trackAdapter);

        // 添加新曲目
        Button addTrackButton = findViewById(R.id.addTrackButton);
        addTrackButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTrackActivity.class);
            startActivity(intent);
        });

        // 实现左滑删除
        setupSwipeToDelete();
    }

    private void loadTracks() {
        apiService.getTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    trackList.clear();
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

                // 删除 Track
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
                        // 处理错误
                    }
                });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
