package com.khoadonguyen.java_music_streaming.Service.Playlist;

import android.util.Log;

import com.khoadonguyen.java_music_streaming.Model.Song;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends ArrayList<Song> {
    private static String tag = "Playlist";
    int index = 0;

    @Override
    public void clear() {

        super.clear();
        index = 0;
        Log.d(tag, "clear playlist thành công");
    }

  public   void next() {

        if (index < this.size()-1) {
            index++;
            Log.d(tag, "current index playlist :" + index);
        }
    }

   public void back() {

        if (index > 0 && index < this.size()) {
            index--;
            Log.d(tag, "current index playlist :" + index);
        }
    }

  public   Song gCurrentSong() {
        Song song = this.get(index);
        Log.d(tag, "playlist trả về bài hát có title :" + song.getTitle());
        return song;
    }
    public int gIndex(){
        return index;
    }

}
