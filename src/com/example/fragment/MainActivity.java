package com.example.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.myapp.R;
/**
 * Created by thamkimdung on 03/08/2016.
 */


public class MainActivity extends Activity {
    private SongListFragment songListFragment = new SongListFragment(R.layout.main);

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, songListFragment).commit();
    }

    public void showPlaySongFragment() {
        transaction = getFragmentManager().beginTransaction();

    }
}
