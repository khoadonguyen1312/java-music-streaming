package com.khoadonguyen.java_music_streaming.presentation.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;

import java.util.List;

public class RecomandAdapter extends RecyclerView.Adapter<RecomandAdapter.ViewHolder> {

    private final Context context;
    private final List<Song> songs;

    public RecomandAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recomand_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);

        // Load ảnh bài hát
        Glide.with(context)
                .load(song.gHighImage())
                      // ảnh khi lỗi load (nếu có)
                .into(holder.thumb);

        // Set tiêu đề
        holder.title.setText(song.getTitle());
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumb;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
        }
    }
}
