package com.khoadonguyen.java_music_streaming.presentation.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.search.SearchView;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl.DynamicAudioPlayerImpl;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicSoundCloudExtractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicYoutubeExtractor;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String tag = "SearchFragment";
    private SearchView searchView;
    private boolean loading = false;


    private DynamicAudioPlayerImpl dynamicAudioPlayer;
    private RecyclerView recyclerView;

    private boolean audioPlayerIsBound = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.recyclerview_search_results);
        handleSearchEvent();


    }

    private void handleSearchEvent() {
        searchView.getEditText().setOnEditorActionListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d(tag, "search");
                String query = searchView.getEditText().getText().toString();
                search(query);
                return true;
            }

            return false;
        });
    }

    private void search(String query) {
        try {

            loading = true;
            List<Song> results = new ArrayList<>();
            AudioPlayerManager.getAudioService().getExtractor().search(query).thenAccept(songs -> {

                getActivity().runOnUiThread(() -> {
                    Log.d(tag, "search thành công");
                    Log.d(tag, "Số lượng kết quả: " + songs.size());

                    SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getContext(), songs);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(searchResultAdapter);
                    searchResultAdapter.setOnItemClickListener(song -> {
                        Log.d(tag, "Mở bài hát có title :" + song.getTitle());

                        AudioPlayerManager.getAudioService().start(song);
                    });
                });
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
