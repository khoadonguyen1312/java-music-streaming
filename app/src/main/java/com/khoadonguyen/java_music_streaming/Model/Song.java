package com.khoadonguyen.java_music_streaming.Model;


import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.SubtitlesStream;

import java.time.Duration;
import java.util.List;

public class Song {
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    private String id;

    private String url;

    private List<AudioStream> audioLink;
    private Source source;
    private String title;

    private List<Image> images;
    private List<SubtitlesStream> subtitlesStreams;

    private Duration duration;


    //    public AudioStream gHighestAudioStream(){
//        return audioLink.stream().map(audioStream -> {
//            audioStream.getb
//
//        });
//
//    }
    public Song(Builder builder) {

        this.subtitlesStreams = builder.subtitlesStreams;
        this.id = builder.getId();
        this.url = builder.getUrl();
        this.audioLink = builder.getAudioLink();
        this.source = builder.getSource();
        this.title = builder.getTitle();
        this.images = builder.getImages();
        this.duration = builder.getDuration();
    }

    public static class Builder {
        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        private String id;
        private Duration duration;
        private String url;

        private List<AudioStream> audioLink;
        private Source source;
        private String title;

        private List<Image> images;
        private List<SubtitlesStream> subtitlesStreams;

        public Song build() {
            return new Song(this);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;

        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder audioLink(List<AudioStream> audioLink) {
            this.audioLink = audioLink;
            return this;

        }

        public Builder source(Source source) {
            this.source = source;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder images(List<Image> images) {
            this.images = images;
            return this;
        }

        public Builder subtitlesStreams(List<SubtitlesStream> subtitlesStreams) {
            this.subtitlesStreams = subtitlesStreams;
            return this;
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
