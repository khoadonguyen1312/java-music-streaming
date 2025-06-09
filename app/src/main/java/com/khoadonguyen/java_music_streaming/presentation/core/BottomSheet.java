package com.khoadonguyen.java_music_streaming.presentation.core;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.extractor.SourceExtractor;

public class BottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "ModalBottomSheet";
    RadioGroup radioGroup;

    RadioButton youtube_radio_button;
    RadioButton soundclound_radio_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_bottom_sheet, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup = view.findViewById(R.id.home_bottom_sheet_radio_group);
        youtube_radio_button = view.findViewById(R.id.youtube_radio_button);
        soundclound_radio_button = view.findViewById(R.id.soundcloud_radio_button);
        initIndexforRadioGroup();
        handleRadioGroup();

    }

    private void initIndexforRadioGroup() {
        int source_id = SourceExtractor.getInstance().getCurrent_source_id();
        if (source_id == 0) {
            radioGroup.check(youtube_radio_button.getId());
        } else {
            radioGroup.check(soundclound_radio_button.getId());
        }

    }

    private void handleRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SourceExtractor.getInstance().changeSource(getContext());
            }
        });
    }
}
