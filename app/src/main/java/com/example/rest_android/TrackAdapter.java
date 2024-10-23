package com.example.rest_android;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    public List<Track> trackList;
    private Context context;


    public TrackAdapter(List<Track> trackList,Context context) {
        this.trackList = trackList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.idTextView.setText(track.getId());
        holder.titleTextView.setText(track.getTitle());
        holder.singerTextView.setText(track.getSinger());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTrackActivity.class); // 使用 context
            intent.putExtra("track_id", track.getId());
            // 如果需要传递其他数据，可以在这里添加
            context.startActivity(intent); // 启动 EditTrackActivity
        });
    }


    public void updateTracks(List<Track> newTracks) {
        trackList.clear();
        trackList.addAll(newTracks);
        notifyDataSetChanged();
    }

    public void removeTrack(int position) {
        if (position >= 0 && position < trackList.size()) {
            trackList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, titleTextView, singerTextView;
        Button editButton;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.trackId);
            titleTextView = itemView.findViewById(R.id.trackTitle);
            singerTextView = itemView.findViewById(R.id.trackSinger);
            editButton = itemView.findViewById(R.id.btnView);
        }
    }
}
