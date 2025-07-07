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

import java.util.Arrays;
import java.util.List;

public class TopMixAdapter extends RecyclerView.Adapter<TopMixAdapter.ViewHolder> {

    private Context context;
    private List<Song> songs;
    static  class Item {
        private int thumb;
        private List<String> popGenres;
        private String label;

        public Item(int thumb, List<String> popGenres, String label) {
            this.thumb = thumb;
            this.popGenres = popGenres;
            this.label = label;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topmix_card, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        Glide.with(context).load(song.gHighImage()).into(holder.thumb);
        

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView label;
        private View bottom_color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.topmix_thumb);
            label = itemView.findViewById(R.id.topmix_label);
            bottom_color = itemView.findViewById(R.id.topmix_bottom_color);

        }
    }


}
