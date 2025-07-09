package com.khoadonguyen.java_music_streaming.presentation.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.TextView;


import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Util.RandomColor;

import java.util.List;

public class SearchFragmentGirdAdapter extends BaseAdapter {
    private List<SearchGirdItem> items;
    private LayoutInflater inflater;

    public SearchFragmentGirdAdapter(List<SearchGirdItem> items, LayoutInflater inflater) {
        this.items = items;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        FrameLayout thumb;
        TextView label;
        ImageView avt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("test", String.valueOf(getCount()));
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_fragment_gird_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.thumb = convertView.findViewById(R.id.search_gird_card_background);
            viewHolder.label = convertView.findViewById(R.id.search_gird_card_label);
            viewHolder.avt = convertView.findViewById(R.id.search_gird_card_thumb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        SearchGirdItem item = items.get(position);
        viewHolder.thumb.setBackgroundColor(RandomColor.randomColor());
        viewHolder.label.setText(item.getLabel_name());
        viewHolder.avt.setImageResource(item.getThumb());

        return convertView;
    }
}
