package com.khoadonguyen.java_music_streaming.presentation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoadonguyen.java_music_streaming.R;

public class RecomendAdapterLoading extends RecyclerView.Adapter<RecomendAdapterLoading.ViewHolder> {
    private int itemcount;

    public RecomendAdapterLoading(int itemcount) {
        this.itemcount = itemcount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recomend_item_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemcount;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
