package com.khoadonguyen.java_music_streaming.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.CurrentPlaylistAdapter;

import java.util.List;

public class CurrentPlaylist extends Fragment {
    private LinearLayout playlist_fragment_nothing;
    private LinearLayout playlist_fragment_playing;
    private RecyclerView playlist_fragment_playlist;
    private MaterialToolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.playlist_fragment, container, false);
    }

    private void setView() {
        playlist_fragment_playlist = getView().findViewById(R.id.playlist_fragment_playlist);
        toolbar = getView().findViewById(R.id.playlist_fragment_toolbar);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
        handlePlaylist();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeScreen.popScreen(getActivity());
            }
        });
    }

    private void handlePlaylist() {
        playlist_fragment_playlist = getView().findViewById(R.id.playlist_fragment_playlist);

        List<Song> songs = AudioPlayerManager.getAudioService().gplaylist();
        CurrentPlaylistAdapter currentPlaylistAdapter = new CurrentPlaylistAdapter(songs, getContext());
        playlist_fragment_playlist.setLayoutManager(new LinearLayoutManager(getContext()));
        playlist_fragment_playlist.setAdapter(currentPlaylistAdapter);

        AudioPlayerManager.getAudioService().getPlaylist().observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist updatedPlaylist) {
                // Cập nhật adapter
                currentPlaylistAdapter.updateData(updatedPlaylist);
                currentPlaylistAdapter.setIndex_song(updatedPlaylist.gIndex());
            }
        });
    }
}
