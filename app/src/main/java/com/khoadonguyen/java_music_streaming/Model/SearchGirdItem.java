package com.khoadonguyen.java_music_streaming.Model;

import com.khoadonguyen.java_music_streaming.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchGirdItem {
    public static final List<SearchGirdItem> items = new ArrayList<>(Arrays.asList(
            new SearchGirdItem("K Pop", R.drawable.kpop, null),
            new SearchGirdItem("Indie", R.drawable.indie, null),
            new SearchGirdItem("R&B", R.drawable.randb, null),
            new SearchGirdItem("pop", R.drawable.pop, null)
    ));

    private String label_name;
    private int thumb;

    private List<String> slugs;

    public SearchGirdItem(String label_name, int thumb, List<String> slugs) {
        this.label_name = label_name;
        this.thumb = thumb;

        this.slugs = slugs;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }


    public List<String> getSlugs() {
        return slugs;
    }

    public void setSlugs(List<String> slugs) {
        this.slugs = slugs;
    }
}
