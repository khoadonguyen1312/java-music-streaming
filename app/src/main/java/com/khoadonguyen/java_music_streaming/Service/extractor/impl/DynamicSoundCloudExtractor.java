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
import org.schabi.newpipe.extractor.stream.StreamInfo;

import java.io.IOException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DynamicSoundCloudExtractor implements Extractor {
    static final String tag = "DynamicSoundCloudExtractor";
    private SoundcloudService soundcloudService;

    @Override
    public CompletableFuture<Song> gsong(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {


                StreamInfo streamInfo = StreamInfo.getInfo(url);
                if (streamInfo != null) {

                    Song song = new Song.Builder().duration(Duration.ofSeconds(streamInfo.getDuration())).author(streamInfo.getSubChannelName()).id(streamInfo.getId()).url(streamInfo.getUrl()).subtitlesStreams(null).audioLink(streamInfo.getAudioStreams()).subtitlesStreams(streamInfo.getSubtitles()).title(streamInfo.getName()).images(streamInfo.getThumbnails()).source(Source.SOUNDCLOUD).build();


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
                                .url(infoitem.getUrl()).source(Source.SOUNDCLOUD).build());
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
        List<CompletableFuture<Song>> futures = new ArrayList<>();

        for (String url : urls) {

            futures.add(gsong(url));
        }

        try {

            CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allDone.join();


            return futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error fetching songs", e);
        }
    }

    @Override
    public SoundcloudService initService() {
        if (soundcloudService == null) {
            synchronized (DynamicSoundCloudExtractor.class) {
                if (soundcloudService == null) {
                    try {
                        soundcloudService = (SoundcloudService) NewPipe.getService(1);
                        Log.d(tag, "khởi tạo thành công soundcloud service");
                    } catch (ExtractionException e) {
                        throw new RuntimeException("Không thể tạo SoundCloud Service", e);
                    }
                }
            }
        }
        return soundcloudService;

    }

    @Override
    public CompletableFuture<List<Song>> recomandSong(Song song) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(tag, "đang lấy video recomend cho video có title :" + song.getTitle());
                List<CompletableFuture<Song>> completableFutureList = new ArrayList<>();
                StreamInfo streamInfo = StreamInfo.getInfo(song.getUrl());
                if (streamInfo != null) {
                    List<InfoItem> infoItems = streamInfo.getRelatedItems();
                    for (var infoitem : infoItems) {
                        completableFutureList.add(gsong(infoitem.getUrl()));
                    }
                    CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
                    List<Song> songs = completableFuture.thenApply(v -> completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList())

                    ).get();
                    List<Song> results = songs.stream().filter(s -> s.getDuration().toMinutes() < 60).collect(Collectors.toList());
                    return results;

                } else {

                    Log.e(tag, "Lấy Không thành công recomend video cho video có tên " + song.getTitle());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return null;
        });
    }
}
