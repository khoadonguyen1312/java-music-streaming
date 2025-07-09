package com.khoadonguyen.java_music_streaming.Util;

import android.graphics.Color;

public class OpacityHexColor {
    public static String darkenColor(String hexColor, float factor) {
        int color = Color.parseColor(hexColor);

        int r = (int) (Color.red(color) * factor);
        int g = (int) (Color.green(color) * factor);
        int b = (int) (Color.blue(color) * factor);

        return String.format("#%06X", (0xFFFFFF & Color.rgb(r, g, b)));
    }

}
