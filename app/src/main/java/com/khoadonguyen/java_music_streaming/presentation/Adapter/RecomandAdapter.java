package com.khoadonguyen.java_music_streaming.presentation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;

import java.util.List;

public class RecomandAdapter extends BaseAdapter {

    private final List<Song> songs;
    private final LayoutInflater inflater;

    public RecomandAdapter(Context context, List<Song> songs) {
        this.songs = songs;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView thumb;
        TextView label;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_recomand_card, parent, false);

            holder = new ViewHolder();
            holder.thumb = convertView.findViewById(R.id.recomand_card_thumb);
            holder.label = convertView.findViewById(R.id.recomand_card_label);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = songs.get(position);
        Glide.with(convertView.getContext())
                .load(song.gHighImage())
                .into(holder.thumb);
        holder.label.setText(song.getTitle());

        return convertView;
    }
}
