package com.khoadonguyen.java_music_streaming.presentation.bottomSheet;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Util.AudioOutputUtil;

public class Output_speaker_bottomsheet extends BottomSheetDialogFragment {
    private LinearLayout open_bluetooth_screen;
    private ImageView icon;
    private TextView name, status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.speaker_output_bottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setSpeakerView();
    }

    private void setView() {
        open_bluetooth_screen = getView().findViewById(R.id.btn_button);
        icon = getView().findViewById(R.id.current_speaker_icon);
        name = getView().findViewById(R.id.current_speaker_name);
        status = getView().findViewById(R.id.bluetooth_check_status);
    }

    private void openBlueToothSetting() {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);

    }

    private void setSpeakerView() {
        status.setVisibility(VISIBLE);
        int icon_speaker = 0;
        open_bluetooth_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBlueToothSetting();
            }
        });

        String name_output = AudioOutputUtil.gAudioOutPutName(requireContext());
        if ("bluetooth".equals(name_output)) {
            icon_speaker = R.drawable.bluetooth;
            status.setVisibility(GONE);
            // Đang phát qua Bluetooth
        } else if ("speaker".equals(name_output)) {
            // Đang phát qua loa ngoài
            icon_speaker = R.drawable.speaker;
        } else if ("headphone".equals(name_output)) {
            // Đang phát qua tai nghe có dây
            icon_speaker = R.drawable.headphone;
        } else if ("earpiece".equals(name_output)) {
            // Đang phát qua loa trong (gần tai)
            icon_speaker = R.drawable.headphone;
        }

        icon.setImageResource(icon_speaker);

        name.setText(name_output);


    }
}
