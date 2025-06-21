package com.khoadonguyen.java_music_streaming.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomColor {

    // Khởi tạo danh sách màu 1 lần duy nhất
    private static final List<Integer> COLOR_LIST = Arrays.asList(
            0xFFFF5252, // Red
            0xFFFFC107, // Amber
            0xFFFF4081, // Pink
            0xFF7C4DFF, // Deep Purple Accent
            0xFF00E676, // Green Accent
            0xFF448AFF, // Blue Accent
            0xFFFF6D00, // Deep Orange Accent
            0xFF69F0AE, // Mint Green
            0xFF18FFFF, // Cyan Accent
            0xFFFFD740  // Yellow Accent
    );

    private static final Random RANDOM = new Random();

    public static int randomColor() {
        return COLOR_LIST.get(RANDOM.nextInt(COLOR_LIST.size()));
    }
}
