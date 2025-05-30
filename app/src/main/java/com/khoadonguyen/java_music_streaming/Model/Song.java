package com.khoadonguyen.java_music_streaming.Model;


import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.SubtitlesStream;

import java.util.List;

public class Song {
    private String id;

    private String url;

    private List<AudioStream> audioLink;
    private Source source;
    private String title;

    private List<Image> images;
    private  List<SubtitlesStream> subtitlesStreams;
    public Song(String id, String url, List<AudioStream> audioLink, Source source, String title, List<Image> images,List<SubtitlesStream>subtitlesStreams) {

        this.subtitlesStreams =subtitlesStreams;
        this.id = id;
        this.url = url;
        this.audioLink = audioLink;
        this.source = source;
        this.title = title;
        this.images = images;
    }


    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public List<AudioStream> getAudioLink() {
        return audioLink;
    }

    public Source getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<SubtitlesStream> getSubtitlesStreams() {
        return subtitlesStreams;
    }
}
