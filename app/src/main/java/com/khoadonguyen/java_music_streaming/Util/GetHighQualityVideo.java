package com.khoadonguyen.java_music_streaming.Util;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetHighQualityVideo {


    public static VideoStream getBestVideoStream(List<VideoStream> streams) {
        if (streams == null || streams.isEmpty()) return null;


        Map<String, Integer> priority = new HashMap<>();
        priority.put("hd1080", 1);
        priority.put("hd720", 2);
        priority.put("large", 3);
        priority.put("medium", 4);
        priority.put("small", 5);
        priority.put("tiny", 6);

        VideoStream best = null;
        int bestRank = Integer.MAX_VALUE;

        for (VideoStream stream : streams) {
            String quality = stream.getQuality();
            Integer rank = priority.get(quality);
            if (rank != null && rank < bestRank) {
                bestRank = rank;
                best = stream;
            }
        }

        return best;
    }

}
