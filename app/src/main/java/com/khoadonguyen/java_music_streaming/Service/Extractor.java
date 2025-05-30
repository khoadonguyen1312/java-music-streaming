package com.khoadonguyen.java_music_streaming.Service;

import android.location.Location;

import com.khoadonguyen.java_music_streaming.Model.Song;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Extractor {

    /**
     * Get Song object from url
     *
     * @param url
     * @return
     */
    public CompletableFuture<Song> gsong(String url);

    //


    /**
     * Search song with query and location
     *
     * @param query
     * @param location
     * @return
     */
    public CompletableFuture<List<Song>> search(String query, Location location);

    /**
     * get many song from list url
     *
     * @param urls
     * @return
     */
    public Song gsong(List<String> urls);

    /**
     * init service
     */

    public StreamingService initService();
}
