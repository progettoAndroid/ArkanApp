package com.example.android.arkanoid;

import android.media.MediaPlayer;

public class MusicCache {
    //classe singleton per la gestione del player musicale
    private static MusicCache musicCache;
    private MediaPlayer mp;

    private MusicCache() {}

    public static MusicCache getInstance(){
        if (musicCache==null)
            musicCache = new MusicCache();
        return musicCache;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }
}
