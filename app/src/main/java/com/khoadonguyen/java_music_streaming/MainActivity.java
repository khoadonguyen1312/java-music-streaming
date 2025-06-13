package com.khoadonguyen.java_music_streaming;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl.DynamicAudioPlayerImpl;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.fragment.CurrentSongFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.FolderFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.HomeFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.SearchFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {
    private static final String tag = "MainActivity";


    //    Button button;
    BottomNavigationView bottomNavigationView;
    private DynamicAudioPlayerImpl audioService;
    private boolean isBound = false;
    private FragmentActivity fragmentActivity = this;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DynamicAudioPlayerImpl.LocalBinder binder = (DynamicAudioPlayerImpl.LocalBinder) service;
            audioService = binder.getService();
            AudioPlayerManager.setAudioService(audioService);
            isBound = true;
            listenPlaylist();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        


        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 5. Logic khác
        replaceFragment(new HomeFragment());
        handleBottomNavs();
        startService();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DynamicAudioPlayerImpl.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }

    private void startService() {
        this.startService(new Intent(this, DynamicAudioPlayerImpl.class));
        Log.d(tag, "khởi tạo thành công AudioPlayer cho toàn bộ ứng dụng");
    }


    private void listenPlaylist() {
        LinearLayout linearLayout = findViewById(R.id.audio_bottomsheet);
        ImageView thumb = findViewById(R.id.audio_bottomsheet_thumb);
        TextView title = findViewById(R.id.audio_bottomsheet_title);
        TextView author = findViewById(R.id.audio_bottomsheet_author);
        LinearLayout audioBottomSheet = findViewById(R.id.audio_bottomsheet);
        int pause_icon = R.drawable.pause_24dp_e3e3e3_fill0_wght400_grad0_opsz24;
        int play_icon = R.drawable.play_arrow_24dp_e3e3e3_fill0_wght400_grad0_opsz24;
        ImageButton play_pause = findViewById(R.id.audio_bottomsheet_pause_play);
        AudioPlayerManager.getAudioService().observePlayer();
        AudioPlayerManager.getAudioService().getPlaylist().observe(this,
                new Observer<Playlist>() {
                    @Override
                    public void onChanged(Playlist songs) {
                        if (!songs.isEmpty()) {
                            Song song = songs.get(songs.gIndex());
                            Log.d(tag, "có bài hát trong playlist");
                            linearLayout.setVisibility(View.VISIBLE);
                            title.setText(song.getTitle());
                            title.setSelected(true);
                            author.setText("ROSE");
                            Glide.with(getApplicationContext()).load(song.getImages().get(0).getUrl()).into(thumb);
                            thumb.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
                    }

                }
        );

        AudioPlayerManager.getAudioService().getPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    play_pause.setImageResource(pause_icon);
                } else {
                    play_pause.setImageResource(play_icon);
                }
            }

        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().togglePausePlay();
            }
        });

        audioBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeScreen.changeScreen(fragmentActivity, new CurrentSongFragment());
            }
        });
    }

}