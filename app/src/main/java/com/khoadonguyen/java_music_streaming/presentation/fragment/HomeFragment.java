package com.khoadonguyen.java_music_streaming.presentation.fragment;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Model.Source;
import com.khoadonguyen.java_music_streaming.R;

import com.khoadonguyen.java_music_streaming.Service.Auth.User;
import com.khoadonguyen.java_music_streaming.Service.extractor.SourceExtractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicYoutubeExtractor;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.Util.RandomSlug;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.ContinuteAdapter;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.ContinuteAdapterLoading;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.RecomandAdapter;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.RecomendAdapterLoading;
import com.khoadonguyen.java_music_streaming.presentation.core.BottomSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    ImageButton playlist_button;
    TextView source_textview;
    GridView recomand_girdView;
    Button test;
    RecyclerView continute_recyclerView, listview2, listview3, listview4;

    private List<Song> recomend_caches = new ArrayList<>();

    private List<Song> history_caches = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        source_textview = view.findViewById(R.id.source_textview);


        playlist_button = view.findViewById(R.id.home_fragment_playlist_button);
        sourceChange();


        playlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator scale = ObjectAnimator.ofPropertyValuesHolder(v, PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1.5f, 1), PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 1.5f, 1));
                scale.setDuration(1000);
                scale.start();
                ChangeScreen.changeScreen(getActivity(), new CurrentPlaylist(), R.anim.slide_in_up, 0, 0, R.anim.slide_in_down);

            }
        });
        int source_id = SourceExtractor.getInstance().getCurrent_source_id();
        if (source_id == 0) {
            source_textview.setText("Youtube");
            source_textview.setTextColor(0xffE14434);

        } else {
            source_textview.setText("SoundCloud");
            source_textview.setTextColor(0xffF68537);
        }
        userInfo();
        recomandView();
        continuteview();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragement, container, false);
    }

    private void sourceChange() {
        source_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet(requireContext());
                bottomSheet.show(getActivity().getSupportFragmentManager(), bottomSheet.getTag());

            }
        });
    }

    private void recomandView() {
        View root = getView();
        if (root == null || !isAdded()) return;

        RecyclerView loadingView = root.findViewById(R.id.recomand_loading);
        RecyclerView doneView = root.findViewById(R.id.recomand_done);

        loadingView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        loadingView.setAdapter(new RecomendAdapterLoading(6));


        loadingView.setVisibility(View.VISIBLE);
        doneView.setVisibility(View.GONE);


        if (recomend_caches != null && !recomend_caches.isEmpty()) {
            doneView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            doneView.setAdapter(new RecomandAdapter(requireContext(), recomend_caches.subList(0, 6)));
            loadingView.setVisibility(View.GONE);
            doneView.setVisibility(View.VISIBLE);
            return;
        }


        executorService.execute(() -> {


            List<Song> songs = SourceExtractor.getInstance().gExtractor(requireContext()).search("lana del rey").join();

            handler.post(() -> {

                if (!isAdded() || getView() == null) return;

                // Cập nhật cache
                if (recomend_caches == null) {
                    recomend_caches = new ArrayList<>();
                } else {
                    recomend_caches.clear();
                }
                recomend_caches.addAll(songs);

                doneView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                doneView.setAdapter(new RecomandAdapter(requireContext(), recomend_caches.subList(0, 6)));
                loadingView.setVisibility(View.GONE);
                doneView.setVisibility(View.VISIBLE);
            });
        });
    }


    private void continuteview() {
        RecyclerView loading = getView().findViewById(R.id.continute_listview_loading);
        ContinuteAdapterLoading continuteAdapterLoading = new ContinuteAdapterLoading(10);
        loading.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        loading.setAdapter(continuteAdapterLoading);
    }

    private void userInfo() {
        ImageView avt = getView().findViewById(R.id.home_fragment_avt);
        TextView email = getView().findViewById(R.id.home_fragment_email);
        String avt_result = new User().getAvt();
        Glide.with(requireContext()).load(avt_result).into(avt);
        email.setText(new User().getEmail());
    }
}
