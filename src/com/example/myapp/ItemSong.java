package com.example.myapp;
/**
 * Created by thamkimdung on 03/08/2016.
 */


public class ItemSong {
    private String dataPath, title, displayName, album, artist;
    private int duration;

    public ItemSong(String dataPath, String title, String displayName, String album, String artist, int duration) {
        this.dataPath = dataPath;
        this.title = title;
        this.displayName = displayName;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }


    @Override
    public String toString() {
        return "a";
    }
}
