package com.khoadonguyen.java_music_streaming.Model;

import java.util.List;

public class PlaylistUi {
    private String url,name;
    private List<Song> songs;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
