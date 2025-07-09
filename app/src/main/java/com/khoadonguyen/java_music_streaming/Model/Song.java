package com.khoadonguyen.java_music_streaming.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.util.Hex;
import com.khoadonguyen.java_music_streaming.Service.realtimedb.LoveSongRespository;

import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.channel.ChannelInfo;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.SubtitlesStream;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Song {
    private static String tag = "Song";
    private String id;
    private String author;
    private String url;
    private List<AudioStream> audioLink;
    private Source source;
    private String title;
    private List<Image> images;
    private List<SubtitlesStream> subtitlesStreams;
    private Duration duration;
    private MediaItem shortThumbVideo;
    private ChannelInfo channelInfo;

    public Song() {
    }

    public Song(Builder builder) {
        this.id = builder.id;
        this.author = builder.author;
        this.url = builder.url;
        this.audioLink = builder.audioLink;
        this.source = builder.source;
        this.title = builder.title;
        this.images = builder.images;
        this.subtitlesStreams = builder.subtitlesStreams;
        this.duration = builder.duration;
        this.channelInfo = builder.channelInfo;
    }


    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
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

    public Duration getDuration() {
        return duration;
    }

    public MediaItem getShortThumbVideo() {
        return shortThumbVideo;
    }

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    } // ✅ GETTER


    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAudioLink(List<AudioStream> audioLink) {
        this.audioLink = audioLink;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setSubtitlesStreams(List<SubtitlesStream> subtitlesStreams) {
        this.subtitlesStreams = subtitlesStreams;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    public void setShortThumbVideo(String videoUrl) {
        if (videoUrl == null) return;

        long startMs = 15000;
        long endMs = 30000;

        MediaItem mediaItem = new MediaItem.Builder().setUri(videoUrl).setClippingConfiguration(new MediaItem.ClippingConfiguration.Builder().setStartPositionMs(startMs).setEndPositionMs(endMs).build()).build();

        this.shortThumbVideo = mediaItem;
    }

    public String gHighImage() {
        if (images == null || images.isEmpty()) return null;

        String maxImageUrl = null;
        int maxArea = 0;

        for (Image image : images) {
            int width = image.getWidth();
            int height = image.getHeight();
            int area = width * height;

            if (area > maxArea) {
                maxArea = area;
                maxImageUrl = image.getUrl();
            }
        }

        return maxImageUrl;
    }

    public void checkIfFacvorite(LoveSongRespository.FavoriteCheckCallback callback) {
        Log.d(tag, "đang check facvorite");
        LoveSongRespository loveSongRespository = new LoveSongRespository();
        loveSongRespository.isFavorite(this, callback);
    }

    public void addfacvorite(Runnable onComplete) {
        LoveSongRespository loveSongRespository = new LoveSongRespository();
        loveSongRespository.addFavorite(this, success -> {
            if (success && onComplete != null) onComplete.run();
        });
    }

    public void removefacvorite(Runnable onComplete) {
        LoveSongRespository loveSongRespository = new LoveSongRespository();
        loveSongRespository.removeFavorite(this, success -> {
            if (success && onComplete != null) onComplete.run();
        });
    }


    public static class Builder {
        private String id;
        private String author;
        private String url;
        private List<AudioStream> audioLink;
        private Source source;
        private String title;
        private List<Image> images;
        private List<SubtitlesStream> subtitlesStreams;
        private Duration duration;
        private ChannelInfo channelInfo; // ✅ THÊM VÀO BUILDER

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
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

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder channelInfo(ChannelInfo channelInfo) {
            this.channelInfo = channelInfo;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }

    public interface OnColorLoadedCallback {
        void onColorReady(String hexColor);

        void onError(Exception e);
    }

    /**
     * lấy màu chủ đạo từ url ảnh
     *
     * @param context
     * @param callback
     */
    public void loadDominantColor(Context context, OnColorLoadedCallback callback) {
        String imageUrl = gHighImage();
        if (imageUrl == null) {
            callback.onColorReady("#888888");
            return;
        }

        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .override(100, 100)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(palette -> {
                            int color = palette.getDominantColor(Color.GRAY);
                            String hex = String.format("#%06X", (0xFFFFFF & color));
                            callback.onColorReady(hex);
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        callback.onError(new Exception("Failed to load image."));
                    }
                });
    }

}
