package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.myapp.*;

/**
 * Created by thamkimdung on 03/08/2016.
 */

public class SongListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView lvSong;
    private MediaManager mediaManager;
    private SongListAdapter mSongListAdapter;

    public SongListFragment(int idLayout) {
        super(idLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return rootView;
    }

    private void initView() {
        lvSong = (ListView) rootView.findViewById(R.id.lv_SongList);
        mediaManager = new MediaManager(getActivity());
        mediaManager.getAllAudioSongs();
        mSongListAdapter = new SongListAdapter(getActivity(), mediaManager.getArrSongs());
        lvSong.setAdapter(mSongListAdapter);
        lvSong.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setBeginSong(position);
        toPlaySongActivity();
    }

    private void toPlaySongActivity() {
        ((MainActivity) getActivity()).showPlaySongFragment();
    }

    private void setBeginSong(int newPosition) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("CurrentSong", newPosition);
        editor.commit();
    }
}
