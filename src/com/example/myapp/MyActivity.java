package com.example.myapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by thamkimdung on 03/08/2016.
 */

public class MyActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView lvSong;
    private MediaManager mediaManager;
    private static SongListAdapter mSongListAdapter;
    private Handler handler = new Handler();
    private boolean run = false;


    private static final String PLAYING = "STATE_PLAY";
    private static final String PAUSE = "STATE_PAUSE";

    private static final String LOOPING = "STATE_LOOPING";
    private static final String NOLOOP = "STATE_NOLOOP";


    private static final String NOSHUFFLE = "STATE_NOSHUFFLE";

    private ImageView btStartPause,  btPrevious, btNext;
    private TextView tvSongName, tvSongArtist, tvIndex, tvTimeSong,tvTimeStart;
    private SeekBar seekBarPlay;
    private static int beginSong;
    private static String state;
    private static String looping;
    private static String shuffling;
    private static boolean IS_RUNNING = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main);
        initView();
    }

    private void initView() {
        lvSong = (ListView) findViewById(R.id.lv_SongList);
        mediaManager = new MediaManager(this);
        mediaManager.getAllAudioSongs();
        mSongListAdapter = new SongListAdapter(this, mediaManager.getArrSongs());
        lvSong.setAdapter(mSongListAdapter);
        lvSong.setOnItemClickListener(this);

        lvSong.setOnItemLongClickListener(this);


        state = PAUSE;
        looping = NOLOOP;
        shuffling = NOSHUFFLE;
        btStartPause = (ImageView) findViewById(R.id.bt_StartPause);

        btPrevious = (ImageView) findViewById(R.id.bt_Previous);
        btNext = (ImageView) findViewById(R.id.bt_Next);

        btStartPause.setOnClickListener(this);

        btPrevious.setOnClickListener(this);
        btNext.setOnClickListener(this);


        tvSongName = (TextView) findViewById(R.id.tv_SongName2);
        tvSongArtist = (TextView) findViewById(R.id.tv_SongArtist2);
        tvIndex = (TextView) findViewById(R.id.tv_Index);
        tvTimeSong = (TextView) findViewById(R.id.tv_SongTime2);

        tvTimeStart= (TextView) findViewById(R.id.timeStart);

        seekBarPlay = (SeekBar) findViewById(R.id.seekBar);
        IS_RUNNING = true;
        startSeekBar.execute();


        seekBarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
            }
        });

        initSongDisplay();

    }

    private void initSongDisplay() {
        tvSongArtist.setText(mediaManager.getCurrentSongName());
        tvSongName.setText(mediaManager.getCurrentArtist());
        seekBarPlay.setMax(mediaManager.getMaxDuration());

        tvIndex.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrSongs().size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_StartPause:
                mediaManager.play();

                if (state == PLAYING) {
                    mediaManager.pause();
                    state = PAUSE;
                    btStartPause.setImageResource(R.drawable.pl);
                } else if (state == PAUSE) {
                    mediaManager.play();
                    state = PLAYING;
                    btStartPause.setImageResource(R.drawable.pp);
                }
                break;
            case R.id.bt_Previous:
                mediaManager.previous();
                break;
            case R.id.bt_Next:
                mediaManager.next();
                break;

        }
    }

    private void seekTo(int progress) {
        mediaManager.seekTo(progress);
    }

    private AsyncTask<Void, Integer, Void> startSeekBar = new AsyncTask<Void, Integer, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            while (IS_RUNNING) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int currentDuration = mediaManager.getCurrentDuration();
                if (currentDuration > 0) {
                    publishProgress(currentDuration);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            seekBarPlay.setProgress(values[0]);
            tvSongArtist.setText(mediaManager.getCurrentSongName());
            tvSongName.setText(mediaManager.getCurrentArtist());
            tvTimeStart.setText(convertToDate(values[0]));

            int a=mediaManager.getMaxDuration();
            seekBarPlay.setMax(a);
            seekBarPlay.setProgress(values[0]);

            tvTimeSong.setText(convertToDate(a));
            tvIndex.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrSongs().size());
            if (state == PLAYING) {
                btStartPause.setImageResource(R.drawable.pp);
            }
        }
    };





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_Backup:
                SharedPreferences sharedPref = this.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                int songIndex = sharedPref.getInt("Song", 0);
                int songTime = sharedPref.getInt("Length", 0);
                if (state == PLAYING) {
                    mediaManager.stop();
                    mediaManager.play(songIndex);
                    mediaManager.seekTo(songTime);
                } else if (state == PAUSE) {
                    mediaManager.stop();
                    mediaManager.play(songIndex);
                    mediaManager.seekTo(songTime);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (IS_RUNNING == true) {
            state = PLAYING;
            mediaManager.stop();

        }
        mediaManager.play(position);






    }



    private void setBeginSong(int newPosition) {

        SharedPreferences sharedPref = this.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("CurrentSong", newPosition);
        editor.commit();
    }

    private void doStart() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateListView();
                        }
                    });
                }


            }
        });
        run = true;
        thread.start();
    }

    private void updateListView() {

        mSongListAdapter.notifyDataSetChanged();
    }


    private String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }

    @Override
    protected void onDestroy() {
        IS_RUNNING = false;

        super.onDestroy();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("Song", mediaManager.getCurrentIndex() - 1);
        editor.putInt("Length", mediaManager.getCurrentDuration());
        editor.commit();

        super.onStop();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,Color.argb(255,179,157,219));
        builder.setView(getLayoutInflater().inflate(R.layout.abc,null));
        builder.setTitle("Thông Tin");
        builder.setMessage("Tên: "+mSongListAdapter.getItem(position).getTitle()
                            +"\nCa sĩ: "+mSongListAdapter.getItem(position).getArtist()
                             +"\nAlbum: "+mSongListAdapter.getItem(position).getAlbum()
                    +"\nThời gian: "+mSongListAdapter.getItem(position).getDuration()
                +"\nPath: "+mSongListAdapter.getItem(position).getDataPath()
        );




        builder.show();

        return true;
    }
}
