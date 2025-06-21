package com.khoadonguyen.java_music_streaming;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl.DynamicAudioPlayerImpl;
import com.khoadonguyen.java_music_streaming.Service.DynamicDownloader;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.fragment.CurrentSongFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.FolderFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.HomeFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.SearchFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.UserFragment;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;

public class MainActivity extends AppCompatActivity {
    private static final String tag = "MainActivity";

    BottomNavigationView bottomNavigationView;
    private DynamicAudioPlayerImpl audioService;
    private boolean isBound = false;
    private final FragmentActivity fragmentActivity = this;

    private LinearLayout bottomSheet;
    private ImageView thumb;
    private TextView title;
    private TextView author;
    private ImageButton playPause;

    private Observer<Playlist> playlistObserver;
    private Observer<Boolean> playingObserver;

    private boolean observersSetup = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DynamicAudioPlayerImpl.LocalBinder binder = (DynamicAudioPlayerImpl.LocalBinder) service;
            audioService = binder.getService();
            AudioPlayerManager.setAudioService(audioService);
            isBound = true;
            setupObservers();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            observersSetup = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        checkCurrentPlaylist();
        if (audioService != null && !observersSetup) {
            setupObservers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Downloader downloader = new DynamicDownloader();
        NewPipe.init(downloader);
        initializeViews();
        replaceFragment(new HomeFragment());
        handleBottomNavs();
        startService();
    }

    private void initializeViews() {
        bottomSheet = findViewById(R.id.audio_bottomsheet);
        thumb = findViewById(R.id.audio_bottomsheet_thumb);
        title = findViewById(R.id.audio_bottomsheet_title);
        author = findViewById(R.id.audio_bottomsheet_author);
        playPause = findViewById(R.id.audio_bottomsheet_pause_play);

        playPause.setOnClickListener(v -> {
            if (AudioPlayerManager.getAudioService() != null) {
                AudioPlayerManager.getAudioService().togglePausePlay();
            }
        });

        bottomSheet.setOnClickListener(v -> ChangeScreen.changeScreen(fragmentActivity, new CurrentSongFragment(), R.anim.slide_in_up, 0, 0, R.anim.slide_in_down));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DynamicAudioPlayerImpl.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            removeObservers();
            unbindService(connection);
            isBound = false;
            observersSetup = false;
        }
    }

    private void handleBottomNavs() {
        bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_1) {
                replaceFragment(new HomeFragment());
                return true;
            }
            if (item.getItemId() == R.id.item_2) {
                replaceFragment(new SearchFragment());
                return true;
            }
            if (item.getItemId() == R.id.item_3) {
                replaceFragment(new FolderFragment());
                return true;
            }
            if (item.getItemId() == R.id.item_4) {
                replaceFragment(new UserFragment());
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    private void startService() {
        this.startService(new Intent(this, DynamicAudioPlayerImpl.class));
        Log.d(tag, "khởi tạo thành công AudioPlayer cho toàn bộ ứng dụng");
    }


    private void setupObservers() {
        if (audioService == null || observersSetup) return;

        int pauseIcon = R.drawable.pause_24dp_e3e3e3_fill0_wght400_grad0_opsz24;
        int playIcon = R.drawable.play_arrow_24dp_e3e3e3_fill0_wght400_grad0_opsz24;

        AudioPlayerManager.getAudioService().observePlayer();

        removeObservers();

        playlistObserver = playlist -> {
            boolean hasSongs = playlist != null && !playlist.isEmpty();
            androidx.fragment.app.Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);

            if (currentFragment instanceof HomeFragment || currentFragment instanceof SearchFragment || currentFragment instanceof FolderFragment || currentFragment instanceof UserFragment) {

                if (hasSongs) {
                    AudioPlayerManager.getAudioService().getPlaylist().observe(this, new Observer<Playlist>() {
                        @Override
                        public void onChanged(Playlist songs) {
                            Song song = songs.get(songs.gIndex());
                            Glide.with(getBaseContext()).load(song.gHighImage()).into(thumb);
                            title.setText(song.getTitle());
                        }
                    });
                }
                showAudioBottom(hasSongs);
            } else {
                showAudioBottom(false);
            }
        };


        playingObserver = isPlaying -> {
            playPause.setImageResource(isPlaying ? pauseIcon : playIcon);
        };

        AudioPlayerManager.getAudioService().getPlaylist().observeForever(playlistObserver);
        AudioPlayerManager.getAudioService().getPlaying().observe(this, playingObserver);

        observersSetup = true;
        Log.d(tag, "Observers setup completed");
    }

    private void removeObservers() {
        if (AudioPlayerManager.getAudioService() != null) {
            if (playlistObserver != null) {
                AudioPlayerManager.getAudioService().getPlaylist().removeObserver(playlistObserver);
            }
            if (playingObserver != null) {
                AudioPlayerManager.getAudioService().getPlaying().removeObserver(playingObserver);
            }
        }
        observersSetup = false;
    }

    private void showAudioBottom(Boolean show) {
        bottomSheet.setVisibility(show ? VISIBLE : GONE);
    }

    private void checkCurrentPlaylist() {
        if (AudioPlayerManager.getAudioService() != null) {
            Playlist current = AudioPlayerManager.getAudioService().getPlaylist().getValue();
            boolean hasSongs = current != null && !current.isEmpty();
            showAudioBottom(hasSongs);
        }
    }
}
