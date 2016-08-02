package com.example.myapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by thamkimdung on 03/08/2016.
 */

public class SongListAdapter extends BaseAdapter {
    private ArrayList<ItemSong> arraySong;
    private LayoutInflater mInflater;
    private int currentPositionPlaying = -1;
    private Context mContext;

    public SongListAdapter(Context context, ArrayList<ItemSong> newArraySong) {
        this.arraySong = newArraySong;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }



    @Override
    public int getCount() {
        return arraySong.size();
    }

    @Override
    public ItemSong getItem(int position) {
        return arraySong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.itemsong, parent, false);
            holder = new ViewHolder();
            holder.tvSongName = (TextView) convertView.findViewById(R.id.tv_SongName);
            holder.tvSongArtist = (TextView) convertView.findViewById(R.id.tv_SongArtist);
            holder.tvSongTime = (TextView) convertView.findViewById(R.id.tv_SongTime);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvSongName.setText(arraySong.get(position).getTitle());
        holder.tvSongArtist.setText(arraySong.get(position).getArtist());

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        holder.tvSongTime.setText(dateFormat.format(new Date(arraySong.get(position).getDuration())));


        final ViewHolder finalHolder = holder;
        return convertView;
    }

    private class ViewHolder {
        TextView tvSongName;
        TextView tvSongArtist;
        TextView tvSongTime;

    }

    private String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }
}
