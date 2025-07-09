package com.khoadonguyen.java_music_streaming.Service.extractor.impl;

import android.util.Log;

import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Model.Source;
import com.khoadonguyen.java_music_streaming.Service.DynamicDownloader;
import com.khoadonguyen.java_music_streaming.Service.extractor.Extractor;
import com.khoadonguyen.java_music_streaming.Util.GetHighQualityVideo;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.channel.ChannelInfo;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;
import org.schabi.newpipe.extractor.stream.VideoStream;


import java.io.IOException;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DynamicYoutubeExtractor implements Extractor {
    final static String tag = "DynamicYoutubeExtractor";
    private static YoutubeService youtubeService;

    @Override
    public CompletableFuture<Song> gsong(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                YoutubeService youtubeService = initService();

                StreamInfo streamInfo = StreamInfo.getInfo(url);

                if (streamInfo != null) {

                    Log.d(tag, "lấy thành công streamExtractor cho video có url :" + url);
                    Song song = new Song.Builder().id(streamInfo.getId()).url(streamInfo.getUrl()).duration(Duration.ofSeconds(streamInfo.getDuration())).subtitlesStreams(streamInfo.getSubtitles()).author(streamInfo.getSubChannelName()).title(streamInfo.getName()).url(streamInfo.getUrl()).images(streamInfo.getThumbnails()).source(Source.YOUTUBE).id(streamInfo.getId()).audioLink(streamInfo.getAudioStreams()).build();
                    List<VideoStream> videoStreams = streamInfo.getVideoOnlyStreams();


                    String video_url = GetHighQualityVideo.getBestVideoStream(videoStreams).getUrl();
                    song.setShortThumbVideo(video_url);

                    Log.d(tag, "tra ve thanh cong song object voi ten :" + song.getTitle());
                    ChannelInfo channelInfo = ChannelInfo.getInfo(streamInfo.getUploaderUrl());
                    song.setChannelInfo(channelInfo);

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

                YoutubeService youtubeService = initService();

                SearchExtractor searchExtractor = youtubeService.getSearchExtractor(query);
                Log.d(tag, "lay thanh cong search extractor");
                if (searchExtractor != null) {
                    Log.d(tag, "lấy thành công search cho query :" + query);
                    searchExtractor.fetchPage();
                    List<InfoItem> infoItems = searchExtractor.getInitialPage().getItems();
                    List<Song> songs = infoItems.stream().filter(item -> item.getInfoType() != InfoItem.InfoType.PLAYLIST).map(item -> new Song.Builder().url(item.getUrl()).title(item.getName()).images(item.getThumbnails()).source(Source.YOUTUBE).build()).collect(Collectors.toList());

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
    public YoutubeService initService() {
        if (youtubeService == null) {
            synchronized (DynamicYoutubeExtractor.class) {
                if (youtubeService == null) {
                    try {
                        youtubeService = (YoutubeService) NewPipe.getService(0);
                        Log.d(tag, "khởi tạo thành công youtube service");
                    } catch (ExtractionException e) {
                        throw new RuntimeException("Không thể khởi tạo YoutubeService", e);
                    }
                }
            }
        }
        return youtubeService;
    }

    @Override
    public CompletableFuture<List<Song>> recomandSong(Song song) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(tag, "đang lấy video recomand cho video có title" + song.getTitle());
                List<CompletableFuture<Song>> completableFutureList = new ArrayList<>();


                StreamInfo streamInfo = StreamInfo.getInfo(song.getUrl());
                if (streamInfo != null) {

                    List<InfoItem> infoItems = streamInfo.getRelatedItems();
                    for (var inforitem : infoItems) {
                        if (!inforitem.getUrl().contains("list")) {
                            completableFutureList.add(gsong(inforitem.getUrl()));
                        }
                    }
                    CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
                    List<Song> songs = completableFuture.thenApply(v -> completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList())

                    ).get();
                    List<Song> results = songs.stream().filter(s -> s.getDuration().toMinutes() < 60).collect(Collectors.toList());

                    return results;

                } else {
                    Log.e(tag, "lấy không thành công recomend video cho video có tên " + song.getTitle());
                }

            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            } catch (ExtractionException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return null;
        });
    }


}
