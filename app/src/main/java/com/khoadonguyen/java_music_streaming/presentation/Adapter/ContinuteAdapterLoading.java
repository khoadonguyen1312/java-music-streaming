package com.khoadonguyen.java_music_streaming.presentation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoadonguyen.java_music_streaming.R;

public class ContinuteAdapterLoading extends RecyclerView.Adapter<ContinuteAdapter.ViewHolder> {
    private int itemcount;

    public ContinuteAdapterLoading(int itemcount) {
        this.itemcount = itemcount;
    }


    @NonNull
    @Override
    public ContinuteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.continuted_card_loading, parent, false);
        return new ContinuteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContinuteAdapter.ViewHolder holder, int position) {

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
