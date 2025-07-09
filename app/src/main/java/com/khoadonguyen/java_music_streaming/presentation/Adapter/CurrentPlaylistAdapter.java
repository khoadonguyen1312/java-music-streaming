package com.khoadonguyen.java_music_streaming.presentation.Adapter;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;

import java.util.List;

public class CurrentPlaylistAdapter extends RecyclerView.Adapter<CurrentPlaylistAdapter.ViewHolder> {
    private List<Song> songs;
    private Context context;

    private int index_song;

    public CurrentPlaylistAdapter(List<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    public void setIndex_song(int index_song) {
        this.index_song = index_song;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().seekToSong(position);
            }
        });
        if (index_song == position) {
            holder.playlist_playing_icon.setVisibility(VISIBLE);
            holder.playlist_title.setTextColor(0xffDC2525);
        } else {

        }
        holder.playlist_title.setText(song.getTitle());
        Glide.with(context).load(song.getImages().get(0).getUrl()).into(holder.playlist_thumb);

    }

    public void updateData(List<Song> newList) {
        this.songs = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout card;
        private ImageView playlist_thumb;
        private TextView playlist_title, playlist_author;
        private ImageButton more_button;

        private SpinKitView playlist_playing_icon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.playlist_card);
            playlist_thumb = itemView.findViewById(R.id.playlist_item_thumb);
            playlist_author = itemView.findViewById(R.id.playlist_item_author);
            playlist_title = itemView.findViewById(R.id.playlist_item_title);
            more_button = itemView.findViewById(R.id.playlist_item_more);
            playlist_playing_icon = itemView.findViewById(R.id.playlist_item_playing_icon);
        }
    }
}
