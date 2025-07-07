package com.khoadonguyen.java_music_streaming.Model;

public class Lyric {
    private String startTime;
    private String endTime;
    private String text;

    public Lyric(String startTime, String endTime, String text) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    // Getter methods
    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "[" + startTime + " - " + endTime + "] " + text;
    }
}
