package com.khoadonguyen.java_music_streaming.Service.extractor.impl;

import android.util.Log;

import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Model.Source;
import com.khoadonguyen.java_music_streaming.Service.DynamicDownloader;
import com.khoadonguyen.java_music_streaming.Service.extractor.Extractor;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.soundcloud.SoundcloudService;

import org.schabi.newpipe.extractor.stream.StreamExtractor;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DynamicSoundCloudExtractor implements Extractor {
    static final String tag = "DynamicSoundCloudExtractor";

    @Override
    public CompletableFuture<Song> gsong(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                SoundcloudService soundcloudService = initService();
                StreamExtractor streamExtractor = soundcloudService.getStreamExtractor(url);
                if (streamExtractor != null) {
                    streamExtractor.fetchPage();
                    Song song = new Song.Builder().id(streamExtractor.getId())
                            .url(streamExtractor.getUrl())
                            .audioLink(streamExtractor.getAudioStreams())
                            .subtitlesStreams(streamExtractor.getSubtitles(MediaFormat.VTT))
                            .title(streamExtractor.getName())
                            .images(streamExtractor.getThumbnails())
                            .source(Source.YOUTUBE)
                            .build();


                    Log.d(tag, "lấy thành công bài hát từ soundcloud có url :" + url);

                    return song;

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
    public CompletableFuture<List<Song>> search(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Song> songs = new ArrayList<>();
                SoundcloudService soundcloudService = initService();
                SearchExtractor searchExtractor = soundcloudService.getSearchExtractor(query);
                if (searchExtractor != null) {
                    searchExtractor.fetchPage();
                    List<InfoItem> infoItems = searchExtractor.getInitialPage().getItems();
                    for (var infoitem : infoItems) {
                        songs.add(new Song.Builder()
                                .title(infoitem.getName())
                                .images(infoitem.getThumbnails())
                                .url(infoitem.getUrl())
                                .source(Source.SOUNDCLOUD)
                                .build()
                        );
                    }
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
        return null;
    }

    @Override
    public SoundcloudService initService() {
        try {
            Downloader downloader = new DynamicDownloader();
            NewPipe.init(downloader);

            SoundcloudService soundcloudService = (SoundcloudService) NewPipe.getService(1);
            Log.d(tag, "Khởi tạo thành công soundcloud service");

            return soundcloudService;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ExtractionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CompletableFuture<List<Song>> recomandSong(Song song) {
        return null;
    }
}
