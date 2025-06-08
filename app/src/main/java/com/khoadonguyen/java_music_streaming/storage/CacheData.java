package com.khoadonguyen.java_music_streaming.storage;

public class CacheData {
    private String currentUrl ;
    private int source=0;


    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
