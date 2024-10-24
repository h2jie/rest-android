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
    private List<Track> trackList;
    private Context context;

    public TrackAdapter(List<Track> trackList, Context context) {
        this.trackList = trackList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.bind(track);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTrackActivity.class);
            intent.putExtra("track_id", track.getId());
            intent.putExtra("track_title", track.getTitle());
            intent.putExtra("track_singer", track.getSinger());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public Track getTrackAt(int position) {
        return trackList.get(position);
    }

    public void updateTracks(List<Track> newTracks) {
        trackList.clear();
        trackList.addAll(newTracks);
        notifyDataSetChanged();
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, titleTextView, singerTextView;
        Button viewButton;

        TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.trackId);
            titleTextView = itemView.findViewById(R.id.trackTitle);
            singerTextView = itemView.findViewById(R.id.trackSinger);

        }

        void bind(Track track) {
            idTextView.setText(track.getId());
            titleTextView.setText(track.getTitle());
            singerTextView.setText(track.getSinger());
        }
    }
}