package com.khoadonguyen.java_music_streaming.presentation.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.search.SearchView;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl.DynamicAudioPlayerImpl;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String tag = "SearchFragment";
    private SearchView searchView;
    private boolean loading = false;

    private GridView topgeneres;
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
        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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


                        new Thread(() -> {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                AudioPlayerManager.getAudioService().start(song);
                            });
                        }).start();

                    });
                });
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
