package com.khoadonguyen.java_music_streaming.Service.extractor.impl;

import android.util.Log;

import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Model.Source;
import com.khoadonguyen.java_music_streaming.Service.DynamicDownloader;
import com.khoadonguyen.java_music_streaming.Service.extractor.Extractor;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.stream.StreamExtractor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DynamicYoutubeExtractor implements Extractor {
    final static String tag = "DynamicYoutubeExtractor";

    @Override
    public CompletableFuture<Song> gsong(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                YoutubeService youtubeService = initService();

                StreamExtractor streamExtractor = youtubeService.getStreamExtractor(url);

                if (streamExtractor != null) {
                    streamExtractor.fetchPage();
                    Log.d(tag, "lấy thành công streamExtractor cho video có url :" + url);
                    Song song = new Song.Builder().title(streamExtractor.getName()).build();

                    Log.d(tag, "tra ve thanh cong song object voi ten :" + song.getTitle());

                    return song;
                }
            } catch (ExtractionException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        });
    }

    @Override
    public CompletableFuture<List<Song>> search(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Song> songs = new ArrayList<>();
                YoutubeService youtubeService = initService();

                SearchExtractor searchExtractor = youtubeService.getSearchExtractor(query);
                Log.d(tag, "lay thanh cong search extractor");
                if (searchExtractor != null) {
                    Log.d(tag, "lấy thành công search cho query :" + query);
                    searchExtractor.fetchPage();
                    List<InfoItem> infoItems = searchExtractor.getInitialPage().getItems();
                    for (var infoitem : infoItems) {
                        Song song = new Song.Builder().url(infoitem.getUrl())
                                .title(infoitem.getName())
                                .images(infoitem.getThumbnails())
                                .source(Source.YOUTUBE)
                                .build();

                        songs.add(song);
                    }
                    Log.d(tag, "láy thành công danh sách video cho search với query :" + query);

                    return songs;


                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            } catch (ExtractionException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return null;
        });
    }

    @Override
    public List<Song> gsong(List<String> urls) {

        try {




         } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public YoutubeService initService() {
        try {
            Downloader downloader = new DynamicDownloader();

            NewPipe.init(downloader);

            YoutubeService youtubeService = (YoutubeService) NewPipe.getService(0);
            Log.d(tag, "khởi tạo thành công youtube service");
            return youtubeService;
        } catch (ExtractionException e) {
            throw new RuntimeException(e);
        }
    }
}
