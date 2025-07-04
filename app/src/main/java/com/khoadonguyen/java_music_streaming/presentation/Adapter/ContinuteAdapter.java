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
import com.khoadonguyen.java_music_streaming.Service.extractor.SourceExtractor;

import java.util.List;

public class ContinuteAdapter extends RecyclerView.Adapter<ContinuteAdapter.ViewHolder> {
    private List<Song> songs;
    private Context context;

    public ContinuteAdapter(Context context, List<Song> songs) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.continue_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        Glide.with(context).load(song.gHighImage()).into(holder.thumb);
        holder.title.setText(song.getTitle());
        int value = SourceExtractor.getInstance().gSource(context);
        if (value == 0) {
            holder.source.setText("Youtube");

        } else {
            holder.source.setText("SoundCloud");
        }

    }


    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView title;
        private TextView source;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.continute_card_thumb);
            title = itemView.findViewById(R.id.continute_card_title);
            source = itemView.findViewById(R.id.continute_card_author);
        }
    }
}
