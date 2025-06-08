package com.khoadonguyen.java_music_streaming.presentation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Model.Source;
import com.khoadonguyen.java_music_streaming.R;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    Context context;
    List<Song> songs;

    /**
     * click song handle
     */
    public interface OnItemClickListener {
        void itemClick(Song song);
    }

    private OnItemClickListener listener;
    public  void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener =listener;
    }
    public SearchResultAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.label.setText(song.getTitle());
        holder.author.setText("ROSE");
        Source source = song.getSource();

        if (source == Source.SOUNDCLOUD) {
            holder.source.setTextColor(0xffFF7601);
        } else {
            holder.source.setTextColor(0xffFF3F33);

        }
        holder.source.setText(source.toString());
        Glide.with(context).load(song.getImages().getLast().getUrl()).into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView label, author, source;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumb = itemView.findViewById(R.id.search_item_result_image);
            label = itemView.findViewById(R.id.search_item_result_title);
            author = itemView.findViewById(R.id.search_item_result_author);
            source = itemView.findViewById(R.id.search_item_result_source);

            itemView.setOnClickListener(v -> {
                int postion = getPosition();
                if (listener != null && postion != RecyclerView.NO_POSITION) {
                    listener.itemClick(songs.get(postion));
                }

            });

        }
    }
}
